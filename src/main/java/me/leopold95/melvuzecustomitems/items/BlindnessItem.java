package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.core.Keys;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.melvuze.melvuzeitemslib.api.Item;

public class BlindnessItem extends Item {

    final double radius = getConfig().getDouble("radius");
    final int blindnessDelay = getConfig().getInt("blindness-delay");
    final double animationSpeed = getConfig().getDouble("animation-speed");
    final PotionEffect blindnessPotion = new PotionEffect(PotionEffectType.BLINDNESS, blindnessDelay * 20, 0);

    public BlindnessItem(Plugin plugin, String key) {
        super(plugin, key);
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        try {
            Sound useSound = Sound.valueOf(getConfig().getString("use-sound"));
            playSoundWithRadius(player.getLocation(), useSound, (float) radius, 100, 1);
        } catch (Exception exp) {
            Bukkit.getConsoleSender().sendMessage(exp.getMessage());
        }

        Bukkit.getScheduler().runTask(CustomItems.plugin, () -> {
            for(double i = 0; i <= 360; i += 0.25){
                double x = Math.sin(i) * radius * 0.5;
                double z = Math.cos(i) * radius * 0.5;
                player.getLocation().getWorld().spawnParticle(Particle.REVERSE_PORTAL ,player.getLocation(), 0,  x, 0, z, animationSpeed);
            }
        });


        for (Entity target : player.getNearbyEntities(radius, radius, radius)) {
            if (!(target instanceof Player))
                continue;

            if(target.getPersistentDataContainer().has(Keys.INVULNERABILITY_ACTIVE, PersistentDataType.INTEGER))
                continue;

            ((Player) target).addPotionEffect(blindnessPotion);
        }
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}

    public void playSoundWithRadius(Location location, Sound sound, float radius, float volume, float pitch) {
        for (Player player : location.getWorld().getPlayers()) {
            if (player.getLocation().distance(location) <= radius) {
                player.playSound(location, sound, volume, pitch);
            }
        }
    }
}
