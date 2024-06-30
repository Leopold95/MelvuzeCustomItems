package me.leopold95.melvuzecustomitems.items;

import me.leopold95.melvuzecustomitems.core.Keys;
import me.leopold95.melvuzecustomitems.CustomItems;
import me.leopold95.melvuzecustomitems.core.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
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
    private CustomItems plugin;

    private final int lifeTime = getConfig().getInt("life-time");
    private final int explodeRange = getConfig().getInt("explode-range");
    private final double velocity = getConfig().getDouble("ball-velocity");

    private final int effectPoisonTime = getConfig().getInt("effect-time.poison");
    private final int effectSlowTime = getConfig().getInt("effect-time.slow");
    private final int effectBlindTime = getConfig().getInt("effect-time.blind");
    private final int effectWeaknessTime = getConfig().getInt("effect-time.weak");

    private final int weakChance = getConfig().getInt("weak-chance");

    private final PotionEffect poison = new PotionEffect(PotionEffectType.POISON, effectPoisonTime, 1);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, effectSlowTime, 1);
    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, effectBlindTime, 1);
    private final PotionEffect weak = new PotionEffect(PotionEffectType.WEAKNESS, effectWeaknessTime, 1);

    public PoisonedCannonballItem(CustomItems plugin, String key) {
        super(plugin, key);
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }

    @Override
    public void onRightClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {
        LargeFireball fireball = player.launchProjectile(LargeFireball.class);

        fireball.getPersistentDataContainer().set(Keys.POISONED_CANNONBALL, PersistentDataType.INTEGER, 1);
        fireball.getPersistentDataContainer().set(Keys.POISONED_CANNONBALL_SENDER, PersistentDataType.STRING, player.getName());

        fireball.setGravity(true);
        fireball.setDirection(player.getLocation().getDirection());
        Vector vel = fireball.getVelocity();
        fireball.setVelocity(vel.multiply(velocity));

        //fireball.setYield(0);
        //fireball.setIsIncendiary(false);

        Bukkit.getScheduler().runTaskLater(CustomItems.plugin, () -> {
            fireball.remove();
        }, lifeTime);
    }

    @Override
    public void onLeftClick(PlayerInteractEvent playerInteractEvent, Player player, ItemStack itemStack) {}

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

        String sound = getConfig().getString("hit-block-sound");
        int volume = getConfig().getInt("hit-block-sound-volume");
        Sounds.playOn(event.getHitEntity().getLocation(), sound, volume, explodeRange);

        Bukkit.getScheduler().runTask(CustomItems.plugin, () -> {
            String senderName = event.getEntity().getPersistentDataContainer().get(Keys.POISONED_CANNONBALL_SENDER, PersistentDataType.STRING);
            poisonNearPlayer(event.getHitEntity().getLocation(), explodeRange, senderName);
        });
    }

    /**
     * Накладывает отрицаельные эффекты на всех игроков в радиусе
     * @param center центер
     * @param radius радиус
     * @param senderName имя отправителя ядра
     */
    private void poisonNearPlayer(Location center, double radius, String senderName){
        for (Player player : center.getNearbyEntitiesByType(Player.class, radius)){
            if(player.getPersistentDataContainer().has(Keys.INVULNERABILITY_ACTIVE, PersistentDataType.INTEGER))
                continue;

            if(player.getName().equals(senderName))
                continue;

            player.addPotionEffect(poison);
            player.addPotionEffect(slow);
            player.addPotionEffect(blind);

            if(rundom(weakChance))
                player.addPotionEffect(weak);

        }

        for (int i = 0; i < 360; i += 30) {
            for (int j = 0; j < 360; j += 30) {
                center.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, center, 1, 0, 0, 0, 0);
            }
        }
    }

    private boolean rundom(int chance){
        return false;
    }
}
