package me.leopold95.melvuzecustomitems.core.animations;

import me.leopold95.melvuzecustomitems.core.Config;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class Animations {
    private static final Particle.DustOptions INVULNER_OPTION = new Particle.DustOptions(Color.BLACK, (float) Config.getDouble("invulner-size"));
    private static final Particle.DustOptions SHIELD_OPTION = new Particle.DustOptions(Color.AQUA, (float) Config.getDouble("shield-size"));
    private static final Particle.DustOptions INFECTION_OPTION = new Particle.DustOptions(Color.GREEN, (float) Config.getDouble("infection-size"));

    public static void infection(Player player){
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 2, INFECTION_OPTION);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(0, 2, 0), 2, INFECTION_OPTION);

        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(.5, 1, .5), 2, INFECTION_OPTION);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(-.5, 1, -.5), 2, INFECTION_OPTION);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(.5, 1, -.5), 2, INFECTION_OPTION);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(-.5, 1, .5), 2, INFECTION_OPTION);
    }

    public static void invulnerability(Player player){
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation(), 2, INVULNER_OPTION);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(0, 2, 0), 2, INVULNER_OPTION);

        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(.5, 1, .5), 2, INVULNER_OPTION);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(-.5, 1, -.5), 2, INVULNER_OPTION);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(.5, 1, -.5), 2, INVULNER_OPTION);
        player.getLocation().getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(-.5, 1, .5), 2, INVULNER_OPTION);
    }

    public static void shield(Player player){
        Location location = player.getLocation();

        //location.getWorld().spawnParticle(Particle.REDSTONE, location.add(.5, 0, .5), 1, option);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 0, .3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 0, -.3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 0, .3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 0, -.3), 1, SHIELD_OPTION);

        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 1, .3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 1, -.3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 1, .3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 1, -.3), 1, SHIELD_OPTION);

        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 2, .3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 2, -.3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(-.3, 2, .3), 1, SHIELD_OPTION);
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(.3, 2, -.3), 1, SHIELD_OPTION);
    }
}
