package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import ru.melvuze.melvuzeitemslib.api.Item;

public class InfectionItem extends Item implements Listener {
    private CustomItems plugin;

    private final double range = getConfig().getDouble("range");
    private final int maxSteps = getConfig().getInt("max-steps");


    public InfectionItem(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}


}
