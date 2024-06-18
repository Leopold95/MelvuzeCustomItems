package me.leopold95.melvuzecustomitems.core;

import me.leopold95.melvuzecustomitems.CustomItems;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.property.SkinIdentifier;
import net.skinsrestorer.api.property.SkinProperty;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class SkinManager {
    private CustomItems plugin;

    private HashMap<UUID, SkinIdentifier> playerSkins;

    public SkinManager(CustomItems plugin){
        this.plugin = plugin;
        playerSkins = new HashMap<>();
    }

    public void setOldSkin(Player player){
        UUID puuid = player.getUniqueId();
        try{
            plugin.skinsRestorer.getPlayerStorage().setSkinIdOfPlayer(puuid, playerSkins.get(puuid));
            plugin.skinsRestorer.getSkinApplier(Player.class).applySkin(player);
            playerSkins.remove(player.getUniqueId());
        } catch (DataRequestException e) {
            e.printStackTrace();
        }
    }

    public void setNewSkin(Player player, String skinName){
        UUID puuid = player.getUniqueId();

        try {
            Optional<SkinIdentifier> property = plugin.skinsRestorer.getPlayerStorage().getSkinIdForPlayer(puuid, player.getName());

            if (property.isPresent()) {
                playerSkins.put(puuid, property.get());

                plugin.skinsRestorer.getPlayerStorage().setSkinIdOfPlayer(puuid, SkinIdentifier.ofCustom(skinName));
                plugin.skinsRestorer.getSkinApplier(Player.class).applySkin(player);
            }
        } catch (DataRequestException e) {
            e.printStackTrace();
        }
    }
}
