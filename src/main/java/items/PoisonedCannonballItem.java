package items;

import core.Keys;
import me.leopold95.melvuzecustomitems.CustomItems;
import models.PoisonedCannonball;
import org.bukkit.Bukkit;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import ru.melvuze.melvuzeitemslib.api.Item;

import java.security.Key;

public class PoisonedCannonballItem  extends Item {
    public static PoisonedCannonball data;


    public PoisonedCannonballItem(Plugin plugin, String key) {
        super(plugin, key);

        data = new PoisonedCannonball();

        data.lifeTime = getConfig().getInt("life-time");
        data.explodeRange = getConfig().getInt("explode-range");
        data.velocity = getConfig().getDouble("ball-velocity");

        data.effectPoisonTime = getConfig().getDouble("effect-time.poison");
        data.effectSlowTime = getConfig().getDouble("effect-time.slow");
        data.effectBlindTime = getConfig().getDouble("effect-time.blind");
        data.effectWeaknessTime = getConfig().getDouble("effect-time.weak");
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        LargeFireball fireball = player.launchProjectile(LargeFireball.class);
        fireball.getPersistentDataContainer().set(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER, 1);
        fireball.setShooter(player);

        fireball.setDirection(player.getLocation().getDirection());
        fireball.setVelocity(fireball.getDirection().multiply(data.velocity));

        fireball.setYield(0);
        fireball.setIsIncendiary(false);

        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, () -> {
            fireball.remove();
        },data.lifeTime * 20L);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {

    }


}
