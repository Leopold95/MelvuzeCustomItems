package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.abstraction.RepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import ru.melvuze.melvuzeitemslib.api.Item;

import java.util.List;

public class TornadoItem extends Item {
    private CustomItems plugin;

    private final double range = getConfig().getDouble("range");
    private final int duration = getConfig().getInt("duration");
    private final int stepTime = getConfig().getInt("step-time");
    private final double blockTpStep = getConfig().getDouble("block-tp-step");
    private final List<String> bannedRegions = getConfig().getStringList("banned-wg-regions");

    public TornadoItem(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        RepeatingTask task =  beginAnimation(player.getLocation());

//        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
//            task.canncel();
//        }, 300);

        new RepeatingTask(plugin, 0, stepTime) {
            int stepsPassed = 0;

            private final Location center = player.getLocation().clone();

            @Override
            public void run() {
                if(stepsPassed == duration){
                    canncel();
                    task.canncel();
                    return;
                }

                for(Player player: center.getNearbyEntitiesByType(Player.class, range)){
                    Location newLocation = center.subtract(blockTpStep, 0, blockTpStep);
                    player.teleport(newLocation);
                }

                stepsPassed++;
            }
        };
    }

    private RepeatingTask beginAnimation(Location center){
        return new RepeatingTask(plugin, 0, 1) {
            private final Particle.DustOptions data = new Particle.DustOptions(Color.AQUA, 3);

            double angle = 0;

            double maxRadius = 2;
            double minRadius = 0.5;
            double currentRadius = maxRadius;
            double radiusStep = -0.035;

            double speed = 0.15;

            double downStep = 0.05;
            double Y = center.getBlockY();
            double maxDown = 3;

            @Override
            public void run() {
                if (angle >= 360)
                    angle = 0;

                if(currentRadius <= minRadius)
                    currentRadius = minRadius;

                if(currentRadius >= maxRadius)
                    currentRadius = maxRadius;

                if(center.getBlockY() - Y >= maxDown){
                    Y = center.getBlockY();
                    currentRadius = maxRadius;
                }

                double x = center.getX() + currentRadius * Math.cos(angle);
                double z = center.getZ() + currentRadius * Math.sin(angle);

                Location particleLocation = new Location(center.getWorld(), x, Y, z);
                center.getWorld().spawnParticle(Particle.REDSTONE, particleLocation, 0, data);

                Y -= downStep;
                angle += speed;
                currentRadius += radiusStep;
            }
        };
    }
}
