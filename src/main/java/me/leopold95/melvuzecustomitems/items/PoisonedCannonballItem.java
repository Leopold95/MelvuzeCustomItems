package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.core.Keys;
import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.core.Sounds;
import me.leopold95.melvuzecustomitems.models.PoisonedCannonball;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
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

        data.effectPoisonTime = getConfig().getInt("effect-time.poison") * 20;
        data.effectSlowTime = getConfig().getInt("effect-time.slow") * 20;
        data.effectBlindTime = getConfig().getInt("effect-time.blind") * 20;
        data.effectWeaknessTime = getConfig().getInt("effect-time.weak") * 20;

        data.poison = new PotionEffect(PotionEffectType.POISON, data.effectPoisonTime, 1);
        data.slow = new PotionEffect(PotionEffectType.SLOW, data.effectSlowTime, 1);
        data.blind = new PotionEffect(PotionEffectType.BLINDNESS, data.effectBlindTime, 1);
        data.weak = new PotionEffect(PotionEffectType.WEAKNESS, data.effectWeaknessTime, 1);
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        LargeFireball fireball = player.launchProjectile(LargeFireball.class);

        fireball.getPersistentDataContainer().set(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER, 1);
        fireball.getPersistentDataContainer().set(Keys.POISONED_CANNONBALL_SENDER, PersistentDataType.STRING, player.getName());

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

        String sound = getConfig().getString("hit-block-sound");
        int volume = getConfig().getInt("hit-block-sound-volume");
        Sounds.playOn(event.getHitBlock().getLocation(), sound, volume, data.explodeRange);

        Bukkit.getScheduler().runTask(CustomItems.plugin, () -> {
            String senderName = event.getEntity().getPersistentDataContainer().get(Keys.POISONED_CANNONBALL_SENDER, PersistentDataType.STRING);
            poisonNearPlayer(event.getHitBlock().getLocation(), data.explodeRange, senderName);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void projectileHitEntity(ProjectileHitEvent event){
        if(!event.getEntity().getPersistentDataContainer().has(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER))
            return;

        if(event.getHitEntity() == null)
            return;

        event.setCancelled(true);
    }

    private void poisonNearPlayer(Location center, double radius, String senderName){
        for (Player player : center.getNearbyEntitiesByType(Player.class, radius)){
            if(player.getPersistentDataContainer().has(Keys.INVULNER_ABILITY, PersistentDataType.INTEGER))
                continue;

            if(player.getName().equals(senderName))
                continue;

            player.addPotionEffect(data.poison);
            player.addPotionEffect(data.slow);
            player.addPotionEffect(data.blind);
            player.addPotionEffect(data.weak);
        }

        Location location = center.clone();

        for (int i = 0; i < 360; i += 10) {
            for (int j = 0; j < 360; j += 10) {
//                double x = Math.sin(Math.toRadians(i)) * Math.cos(Math.toRadians(j));
//                double y = Math.sin(Math.toRadians(i)) * Math.sin(Math.toRadians(j));
//                double z = Math.cos(Math.toRadians(i));
                location.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, location, 1, 0, 0, 0, 0);
            }
        }
    }
}
