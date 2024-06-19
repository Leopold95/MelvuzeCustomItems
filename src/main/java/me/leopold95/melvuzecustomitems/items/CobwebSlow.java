package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.abstraction.RepeatingTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
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

    private final int speedDuration = getConfig().getInt("speed-duration");
    private final int speedAmplifier = getConfig().getInt("speed-amplifier");
    private final PotionEffect effectSpeed = new PotionEffect(PotionEffectType.SPEED, speedDuration, speedAmplifier);

    public CobwebSlow(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        final Vector eyeLocation = player.getEyeLocation().toVector();
        final Vector direction = player.getEyeLocation().getDirection().multiply(range);

        player.addPotionEffect(effectSpeed);
        spawnCobweb(eyeLocation, direction, player.getWorld());

        Bukkit.getScheduler().runTaskLater(plugin,
            ()  -> removeCobweb(eyeLocation, direction, player.getWorld()),
        returnAirTime);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}

    private void spawnCobweb(Vector eyeLocation, Vector direction, World world) {
        final BlockIterator iter = new BlockIterator(world, eyeLocation, direction, 0, range);

        new RepeatingTask(plugin, 0, ticksForBlockReplace) {
            @Override
            public void run() {
                if (iter != null && iter.hasNext() == false)
                    canncel();

                Block block = iter.next();
                Location location = block.getLocation();

                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    Location playerLocation = onlinePlayer.getLocation();
                    if (playerLocation.getBlockX() == location.getBlockX() &&
                            playerLocation.getBlockY() == location.getBlockY() &&
                            playerLocation.getBlockZ() == location.getBlockZ()) {





                        //player.sendMessage("На блоке " + block.getType().toString() + " находится игрок " + onlinePlayer.getName());
                    }
                }

                if (block.getType() == Material.AIR)
                    block.setType(Material.COBWEB);
            }
        };
    }

    private void removeCobweb(Vector eyeLocation, Vector direction, World world) {
        final BlockIterator iter = new BlockIterator(world, eyeLocation, direction, 0, range);

        new RepeatingTask(plugin, 0, ticksForBlockReplace) {
            @Override
            public void run() {
                if (iter != null && iter.hasNext() == false)
                    canncel();

                Block block = iter.next();
                if (block.getType() == Material.COBWEB)
                    block.setType(Material.AIR);
            }
        };
    }

    private void tryHurtPlayer(){

    }
}
