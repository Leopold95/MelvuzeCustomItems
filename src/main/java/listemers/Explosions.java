package listemers;

import core.Keys;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataType;

import javax.swing.text.html.parser.Entity;

public class Explosions implements Listener {
    @EventHandler
    private void onCannonballExplodes(EntityExplodeEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        event.setCancelled(true);
    }
}
