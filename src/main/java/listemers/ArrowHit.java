package listemers;

import core.Keys;
import org.bukkit.entity.Arrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.persistence.PersistentDataType;

public class ArrowHit implements Listener {
    @EventHandler
    private void onArrowHitPoisonedCannonBall(ProjectileHitEvent event){
        if(!(event.getEntity() instanceof Arrow))
            return;

        if(event.getHitEntity() == null && !event.getHitEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        System.out.println("arrow hit cannonball");
    }
}
