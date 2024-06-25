package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.core.Keys;
import me.leopold95.melvuzecustomitems.core.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import ru.melvuze.melvuzeitemslib.api.Item;

public class InfectionItem extends Item implements Listener {
    private CustomItems plugin;

    private final double range = getConfig().getDouble("range");
    private final int maxSteps = getConfig().getInt("max-steps");
    private final int protectionDelay = getConfig().getInt("protection-delay");

    private final String gotSound = getConfig().getString("infection-got-sound");
    private final int gotSoundVolume = getConfig().getInt("infection-got-sound-volume");

    private final String endSound = getConfig().getString("infection-end-sound");
    private final int endSoundVolume = getConfig().getInt("infection-end-sound-volume");

    public InfectionItem(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        Player hittedPlayer = tryHitPlayer(player);

        if(hittedPlayer == null)
            return;

        if (hittedPlayer.getPersistentDataContainer().has(Keys.INFECTION_PROTECT, PersistentDataType.INTEGER))
            return;

        hittedPlayer.sendMessage("u get infection");
        hittedPlayer.getPersistentDataContainer().set(Keys.INFECTION_STEP, PersistentDataType.INTEGER, 1);
        Sounds.playTo(hittedPlayer, gotSound, gotSoundVolume);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}

    @EventHandler
    private void onPlayerAction(PlayerInteractEvent event){
        if(!event.getAction().isRightClick())
            return;

        Player hittedPlayer = tryHitPlayer(event.getPlayer());

        if(hittedPlayer == null)
            return;

        if (hittedPlayer.getPersistentDataContainer().has(Keys.INFECTION_PROTECT, PersistentDataType.INTEGER))
            return;

        Player player = event.getPlayer();

        int ownInfectionLvl = player.getPersistentDataContainer().get(Keys.INFECTION_STEP, PersistentDataType.INTEGER);

        if(ownInfectionLvl > maxSteps)
            return;

        hittedPlayer.getPersistentDataContainer().set(Keys.INFECTION_STEP, PersistentDataType.INTEGER, ownInfectionLvl + 1);
    }

    private void setProtectDelay(Player player){
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.getPersistentDataContainer().remove(Keys.INFECTION_PROTECT);
        }, protectionDelay);
    }

    private void playAnimation(Player player){

    }

    private Player tryHitPlayer(Player whoCasted){
        World whoCastedWorld = whoCasted.getWorld();
        Vector direction = whoCasted.getEyeLocation().getDirection().multiply(range);
        Vector eyeLocation = whoCasted.getEyeLocation().toVector();

        BlockIterator rayCastIterator = new BlockIterator(whoCastedWorld, eyeLocation, direction, 0, (int)range);

        while (rayCastIterator.hasNext()){
            Location iterationLocation = rayCastIterator.next().getLocation();

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                Location playerLocation = onlinePlayer.getLocation();
                Location playerHeadLocation = onlinePlayer.getLocation().add(0, 1, 0);

                if (playerLocation.getBlockX() == iterationLocation.getBlockX() && playerLocation.getBlockY() == iterationLocation.getBlockY() &&
                        playerLocation.getBlockZ() == iterationLocation.getBlockZ()) {
                    if(onlinePlayer.equals(whoCasted))
                        continue;

                    return onlinePlayer;
                }
                else if(playerHeadLocation.getBlockX() == iterationLocation.getBlockX() && playerHeadLocation.getBlockY() == iterationLocation.getBlockY() &&
                        playerHeadLocation.getBlockZ() == iterationLocation.getBlockZ()) {
                    if(onlinePlayer.equals(whoCasted))
                        continue;

                    return onlinePlayer;
                }
            }
        }

        return null;
    }
}
