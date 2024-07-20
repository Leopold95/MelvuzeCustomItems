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
import ru.melvuze.melvuzeitemslib.api.Item;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class TornadoItem extends Item {
    private CustomItems plugin;
    private Item item;

    private final double range = getConfig().getDouble("range");
    private final int duration = getConfig().getInt("duration");
    private final int stepTime = getConfig().getInt("step-time");
    private final double blockTpStep = getConfig().getDouble("block-tp-step");
    private final List<String> bannedRegions = getConfig().getStringList("banned-wg-regions");
    private final Particle.DustOptions data = new Particle.DustOptions(Color.AQUA, 3);

    public TornadoItem(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;
        item = this;
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        Location loc = player.getLocation().clone();

        RepeatingTask task =  beginAnimation(loc);
        AtomicReference<RepeatingTask> task1 = new AtomicReference<>();
        
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            task1.set(beginAnimation(loc));
        }, getConfig().getInt("animation.spawn-second"));

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            task.canncel();
            task1.get().canncel();
        }, 300);

//        new RepeatingTask(plugin, 0, stepTime) {
//            int stepsPassed = 0;
//
//            private final Location center = player.getLocation().clone();
//
//            @Override
//            public void run() {
//                if(stepsPassed == duration){
//                    canncel();
//                    task.canncel();
//                    return;
//                }
//
//                for(Player player: center.getNearbyEntitiesByType(Player.class, range)){
//                    Location newLocation = center.subtract(blockTpStep, 0, blockTpStep);
//                    player.teleport(newLocation);
//                }
//
//                stepsPassed++;
//            }
//        };
    }

    private RepeatingTask beginAnimation(Location center){
        TornadoAnimation animation = new TornadoAnimation(center.clone(), data, item, false);
        TornadoAnimation animation1 = new TornadoAnimation(center.clone(), data, item, true);


        return new RepeatingTask(plugin, 0, 1) {


            @Override
            public void run() {
                animation.update();
                animation1.update();
            }
        };
    }
}

class TornadoAnimation{
    private Location center;
    private Particle.DustOptions data;
    private boolean reversed;

    private double angle = 0;

    private double maxRadius;
    private double minRadius;
    private double currentRadius;
    private double radiusStep;

    private double speed;

    private double downStep;
    private double Y;
    private double maxDown;

    public TornadoAnimation(Location center, Particle.DustOptions data, Item item, boolean reversed){
        this.center = center;
        this.data = data;
        this.reversed = reversed;

        maxRadius = item.getConfig().getDouble("animation.radius.max");
        minRadius = item.getConfig().getDouble("animation.radius.min");
        currentRadius = maxRadius;
        radiusStep = item.getConfig().getDouble("animation.radius.step");
        speed = item.getConfig().getDouble("animation.speed");
        downStep = item.getConfig().getDouble("animation.down.step");
        Y = center.getBlockY();
        maxDown = item.getConfig().getDouble("animation.down.max");;
    }

    public void update(){
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

        if(reversed){
            Y -= downStep;
            angle -= speed;
            currentRadius += radiusStep;
            return;
        }

        Y -= downStep;
        angle += speed;
        currentRadius += radiusStep;
    }
}