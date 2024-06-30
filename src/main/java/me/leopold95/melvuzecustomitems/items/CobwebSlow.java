package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.abstraction.RepeatingTask;
import me.leopold95.melvuzecustomitems.core.Keys;
import me.leopold95.melvuzecustomitems.core.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import ru.melvuze.melvuzeitemslib.api.Item;

import java.util.ArrayList;
import java.util.List;

public class CobwebSlow extends Item implements Listener {
    private CustomItems plugin;

    private final int range = getConfig().getInt("range");
    private final int ticksForBlockReplace = getConfig().getInt("ticks-for-block-replace");
    private final int returnAirTime = getConfig().getInt("return-air-time");

    private final int speedDuration = getConfig().getInt("user.speed-duration");
    private final int speedAmplifier = getConfig().getInt("user.speed-amplifier");
    private final PotionEffect effectSpeed = new PotionEffect(PotionEffectType.SPEED, speedDuration, speedAmplifier);

    private final int vampirismDuration = getConfig().getInt("user.vampirism-duration");
    private final double vampirismGiveHealth = getConfig().getDouble("user.vampirism-given-health");
    private final String vampirismGiveSound = getConfig().getString("user.vampirism-given-sound");
    private final int vampirismGiveSoundVolume = getConfig().getInt("user.vampirism-given-sound-volume");
    private final String vampirismGiveMessage = getConfig().getMessage("user.vampirism-given-sound-message");
    private final String vampirismRemoveMessage = getConfig().getMessage("user.vampirism-removed-sound-message");

    private final int targetSlownessAmplifier = getConfig().getInt("target.slowness-amplifier");
    private final int targetSlownessDuration = getConfig().getInt("target.slowness-duration");
    private final PotionEffect targetSlowness = new PotionEffect(PotionEffectType.SLOW, targetSlownessDuration, targetSlownessAmplifier);

    public CobwebSlow(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        final Vector eyeLocation = player.getEyeLocation().toVector();
        final Vector direction = player.getEyeLocation().getDirection().multiply(range);

        player.addPotionEffect(effectSpeed);
        spawnCobweb(eyeLocation, direction, player.getWorld(), player);

        Bukkit.getScheduler().runTaskLater(plugin, ()  -> {
            removeCobweb(eyeLocation, direction, player.getWorld());
        }, returnAirTime);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}

    @EventHandler
    private void onPlayerAttacking(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player player))
            return;

        if(!(event.getEntity() instanceof Player targetPlayer))
            return;

        if(!player.getPersistentDataContainer().has(Keys.VAMPIRISM_ACTIVE, PersistentDataType.INTEGER))
            return;

        double currentHealth = player.getHealth();
        double newHealth = currentHealth + vampirismGiveHealth;

        if(newHealth > player.getMaxHealth()){
            player.setHealth(player.getMaxHealth());
            return;
        }

        player.setHealth(newHealth);
    }

    private void spawnCobweb(Vector eyeLocation, Vector direction, World world, Player whoCasted) {
        final BlockIterator iter = new BlockIterator(world, eyeLocation, direction, 0, range);

        new RepeatingTask(plugin, 0, ticksForBlockReplace) {
            @Override
            public void run() {
                if (!iter.hasNext()){
                    canncel();
                    return;
                }

                Block block = iter.next();

                if(block.getType() != Material.AIR){
                    canncel();
                    return;
                }

                Location cobwebLocation = block.getLocation();

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Location playerLocation = onlinePlayer.getLocation();
                    Location playerHeadLocation = onlinePlayer.getLocation().add(0, 1, 0);

                    if (playerLocation.getBlockX() == cobwebLocation.getBlockX() && playerLocation.getBlockY() == cobwebLocation.getBlockY() &&
                        playerLocation.getBlockZ() == cobwebLocation.getBlockZ()) {

                        if(onlinePlayer.equals(whoCasted))
                            continue;

                        targetFound(onlinePlayer, whoCasted);
                        canncel();
                        break;
                    }
                    else if(playerHeadLocation.getBlockX() == cobwebLocation.getBlockX() && playerHeadLocation.getBlockY() == cobwebLocation.getBlockY() &&
                        playerHeadLocation.getBlockZ() == cobwebLocation.getBlockZ()) {

                        if(onlinePlayer.equals(whoCasted))
                            continue;

                        targetFound(onlinePlayer, whoCasted);
                        canncel();
                        break;
                    }
                }

                if (block.getType() == Material.AIR)
                    block.setType(Material.COBWEB);
            }
        };
    }

    private void targetFound(Player target, Player whoCasted){
        target.addPotionEffect(targetSlowness);
        giveVampirism(whoCasted);
    }

    /**
     * Удаляет запавненную паутину
     * @param eyeLocation
     * @param direction
     * @param world
     */
    private void removeCobweb(Vector eyeLocation, Vector direction, World world) {
        final BlockIterator iter = new BlockIterator(world, eyeLocation, direction, 0, range);

        new RepeatingTask(plugin, 0, ticksForBlockReplace) {
            @Override
            public void run() {
                if (!iter.hasNext()){
                    canncel();
                    return;
                }


                Block block = iter.next();
                if (block.getType() == Material.COBWEB)
                    block.setType(Material.AIR);
            }
        };
    }

    /**
     * Дает еффет вампиризма цели
     * @param player цель наложения эффекта
     */
    private void giveVampirism(Player player){
        player.getPersistentDataContainer().set(Keys.VAMPIRISM_ACTIVE, PersistentDataType.INTEGER, 1);
        Sounds.playTo(player, vampirismGiveSound, vampirismGiveSoundVolume);
        player.sendActionBar(vampirismGiveMessage);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.getPersistentDataContainer().remove(Keys.VAMPIRISM_ACTIVE);
            player.sendActionBar(vampirismRemoveMessage);
        }, vampirismDuration);
    }
}
