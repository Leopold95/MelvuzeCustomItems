package me.leopold95.melvuzecustomitems.listeners;

import me.leopold95.melvuzecustomitems.core.Keys;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

public class PlayerJoin implements Listener {
    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if(player.getPersistentDataContainer().has(Keys.VAMPIRISM_ACTIVE, PersistentDataType.INTEGER))
            player.getPersistentDataContainer().remove(Keys.VAMPIRISM_ACTIVE);

        if(player.getPersistentDataContainer().has(Keys.INVULNERABILITY_ACTIVE, PersistentDataType.INTEGER))
            player.getPersistentDataContainer().remove(Keys.INVULNERABILITY_ACTIVE);

        if(player.getPersistentDataContainer().has(Keys.SHIELD_ACTIVE, PersistentDataType.INTEGER))
            player.getPersistentDataContainer().remove(Keys.SHIELD_ACTIVE);
    }
}
