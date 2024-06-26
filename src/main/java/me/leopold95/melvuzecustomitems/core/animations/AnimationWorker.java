package me.leopold95.melvuzecustomitems.core.animations;

import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.abstraction.RepeatingTask;
import me.leopold95.melvuzecustomitems.core.Keys;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class AnimationWorker{
    private CustomItems plugin;
    private int updateTicks;

    public AnimationWorker(CustomItems plugin, int updateTicks){
        this.plugin = plugin;
        this.updateTicks = updateTicks;
    }


    public void run(){
        new RepeatingTask(plugin, 0, updateTicks) {
            @Override
            public void run() {
                for (Player player: Bukkit.getOnlinePlayers()){
                    if(player.getPersistentDataContainer().has(Keys.INVULNERABILITY_ACTIVE, PersistentDataType.INTEGER))
                        Animations.invulnerability(player);

                    if(player.getPersistentDataContainer().has(Keys.SHIELD_HEALTH, PersistentDataType.DOUBLE))
                        Animations.shield(player);

                    if(player.getPersistentDataContainer().has(Keys.INFECTION_STEP, PersistentDataType.INTEGER))
                        Animations.infection(player);
                }
            }
        };
    }
}
