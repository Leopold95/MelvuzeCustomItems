package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.abstraction.RepeatingTask;
import me.leopold95.melvuzecustomitems.core.Keys;
import me.leopold95.melvuzecustomitems.core.Sounds;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import ru.melvuze.melvuzeitemslib.Main;
import ru.melvuze.melvuzeitemslib.api.Item;

public class ShieldItem extends Item implements Listener {
    private CustomItems plugin;

    final int animationTicks = getConfig().getInt("play-animation-tick");

    final int maxActiveTime = getConfig().getInt("max-active-time");
    final double additionalHealth = getConfig().getDouble("additional-health");

    final String hitSound = getConfig().getString("hit-sound");
    final int hitSoundVolume = getConfig().getInt("hit-sound-volume");

    final String activatedSound = getConfig().getString("activated-sound");
    final int activatedSoundVolume = getConfig().getInt("activated-sound-volume");

    final String endedSound = getConfig().getString("ended-sound");
    final int endedVolume = getConfig().getInt("ended-sound-volume");

    private final Particle.DustOptions option = new Particle.DustOptions(Color.AQUA, 0.5f);

    public ShieldItem(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;

        this.plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        playShieldActiveAnimation(player, maxActiveTime, animationTicks);

        Sounds.playTo(player, activatedSound, activatedSoundVolume);
        player.getPersistentDataContainer().set(Keys.SHIELD_HEALTH, PersistentDataType.DOUBLE, additionalHealth);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(player.getPersistentDataContainer().has(Keys.SHIELD_HEALTH, PersistentDataType.DOUBLE))
                return;
            player.getPersistentDataContainer().remove(Keys.SHIELD_HEALTH);
            Sounds.playTo(player, endedSound, endedVolume);
        }, maxActiveTime);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}

    @EventHandler
    private void onDamageGotten(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player damager))
            return;

        if(!(event.getEntity() instanceof Player target))
            return;

        if(!target.getPersistentDataContainer().has(Keys.SHIELD_HEALTH, PersistentDataType.DOUBLE))
            return;

        double currntSheild = target.getPersistentDataContainer().get(Keys.SHIELD_HEALTH, PersistentDataType.DOUBLE);
        double damage = event.getDamage();

        if(currntSheild >= damage){
            event.setCancelled(true);

            currntSheild -= damage;
            target.getPersistentDataContainer().set(Keys.SHIELD_HEALTH, PersistentDataType.DOUBLE, currntSheild);
        } else {
            target.getPersistentDataContainer().remove(Keys.SHIELD_HEALTH);
            double newDamage = currntSheild - damage;
            event.setDamage(newDamage);
        }


        Sounds.playTo(damager, hitSound, hitSoundVolume);
    }

    private void playShieldActiveAnimation(Player player, int duration, int update){
            new RepeatingTask(plugin, 0, update) {
            long ticksPassed;

            @Override
            public void run() {
                if(ticksPassed == duration || !player.getPersistentDataContainer().has(Keys.SHIELD_HEALTH, PersistentDataType.DOUBLE)){
                    canncel();
                    player.getPersistentDataContainer().remove(Keys.SHIELD_HEALTH);
                    Sounds.playTo(player, endedSound, endedVolume);
                    return;
                }

                Location location = player.getLocation();

                //location.getWorld().spawnParticle(Particle.REDSTONE, location.add(.5, 0, .5), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 0, .3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 0, -.3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 0, .3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 0, -.3), 1, option);

                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 1, .3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 1, -.3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 1, .3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 1, -.3), 1, option);

                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 2, .3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 2, -.3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 2, .3), 1, option);
                location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 2, -.3), 1, option);

                ticksPassed++;
            }
        };
    }
}
