package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;
import ru.melvuze.melvuzeitemslib.api.Item;

public class CobwebSlow extends Item implements Listener {
    private CustomItems plugin;

    private final int range = getConfig().getInt("range");

    public CobwebSlow(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        Block targetBlock = player.getTargetBlock(range); // Получаем блок, на который смотрит игрок на расстоянии до 10 блоков

        Location eyeLocation = player.getEyeLocation();
        Vector direction = player.getEyeLocation().getDirection().multiply(range);

        BlockIterator iter = new BlockIterator(player.getWorld(), eyeLocation.toVector(), direction, 0, range);

        while (iter.hasNext()) {
            Block block = iter.next();
            block.setType(Material.COBWEB);

//            if (block.getType() != Material.AIR) {
//                // Ray hit a block
//                player.sendMessage("Hit block at: " + block.getLocation());
//                break;
//            }
        }

        //System.out.println(targetBlock);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {

    }
}
