package me.leopold95.melvuzecustomitems.listemers;

import me.leopold95.melvuzecustomitems.core.Keys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;

public class ProjectileHit implements Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    private void projectileHitBlock(ProjectileHitEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        if(event.getHitBlock() == null)
            return;

        event.setCancelled(true);


        System.out.println("hit block");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void projectileHitEntity(ProjectileHitEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        if(event.getHitEntity() == null)
            return;

        event.setCancelled(true);

        System.out.println("hit entity");
    }
}
