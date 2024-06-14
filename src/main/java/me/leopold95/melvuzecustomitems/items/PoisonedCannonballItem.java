package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.core.Keys;
import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.models.PoisonedCannonball;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import ru.melvuze.melvuzeitemslib.api.Item;

public class PoisonedCannonballItem  extends Item implements Listener {
    public static PoisonedCannonball data;

    private CustomItems plugin;

    public PoisonedCannonballItem(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);

        data = new PoisonedCannonball();

        data.lifeTime = getConfig().getInt("life-time");
        data.explodeRange = getConfig().getInt("explode-range");
        data.velocity = getConfig().getDouble("ball-velocity");

        data.effectPoisonTime = getConfig().getInt("effect-time.poison");
        data.effectSlowTime = getConfig().getInt("effect-time.slow");
        data.effectBlindTime = getConfig().getInt("effect-time.blind");
        data.effectWeaknessTime = getConfig().getInt("effect-time.weak");

        data.poison = new PotionEffect(PotionEffectType.POISON, data.effectPoisonTime, 1);
        data.slow = new PotionEffect(PotionEffectType.SLOW, data.effectSlowTime, 1);
        data.blind = new PotionEffect(PotionEffectType.BLINDNESS, data.effectBlindTime, 1);
        data.weak = new PotionEffect(PotionEffectType.WEAKNESS, data.effectWeaknessTime, 1);
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        LargeFireball fireball = player.launchProjectile(LargeFireball.class);

        fireball.getPersistentDataContainer().set(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER, 1);

        fireball.setGravity(true);
        fireball.setDirection(player.getLocation().getDirection());
        fireball.setVelocity(new Vector(1, 1, 1).multiply(0));

        //fireball.setYield(0);
        //fireball.setIsIncendiary(false);

        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, () -> {
            fireball.remove();
        },data.lifeTime * 20L);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}

    @EventHandler
    private void onCannonballLaunched(ProjectileLaunchEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        //event.getEntity().setGravity(false);
        //event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(data.velocity));
    }

    @EventHandler
    private void onArrowHitPoisonedCannonBall(ProjectileHitEvent event){
        if(!(event.getEntity() instanceof Arrow))
            return;

        if(event.getHitEntity() == null && !event.getHitEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        System.out.println("arrow hit cannonball");
    }

    @EventHandler
    private void onCannonballExplodes(EntityExplodeEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void projectileHitBlock(ProjectileHitEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        if(event.getHitBlock() == null)
            return;

        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void projectileHitEntity(ProjectileHitEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        if(event.getHitEntity() == null)
            return;

        event.setCancelled(true);
    }

    private void poisonNearPlayer(Location center, double radius){
        for (Entity ent : center.getNearbyEntities(radius, radius, radius)){
            if(!(ent instanceof Player))
                continue;

            ((Player) ent).addPotionEffect(data.poison);
            ((Player) ent).addPotionEffect(data.slow);
            ((Player) ent).addPotionEffect(data.blind);
            ((Player) ent).addPotionEffect(data.weak);
        }
    }
}
