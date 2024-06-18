package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.core.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.melvuze.melvuzeitemslib.api.Item;

public class ShieldItem extends Item implements Listener {
    private CustomItems plugin;

    final int maxActiveTime = getConfig().getInt("max-active-time");
    final int additionalHealth = getConfig().getInt("additional-health");

    final String hitSound = getConfig().getString("hit-sound");
    final int hitSoundVolume = getConfig().getInt("hit-sound-volume");

    final String activatedSound = getConfig().getString("activated-sound");
    final int activatedSoundVolume = getConfig().getInt("activated-sound-volume");

    public ShieldItem(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        Sounds.playTo(player, activatedSound, activatedSoundVolume);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {

        }, maxActiveTime * 20L);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}

    @EventHandler
    private void onDamageGotten(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))
            return;

    }
}
