package me.leopold95.melvuzecustomitems;

import me.leopold95.melvuzecustomitems.items.BlindnessItem;
import me.leopold95.melvuzecustomitems.items.InvulnerabilityItem;
import me.leopold95.melvuzecustomitems.items.PoisonedCannonballItem;
import me.leopold95.melvuzecustomitems.listemers.ArrowHit;
import me.leopold95.melvuzecustomitems.listemers.Explosions;
import me.leopold95.melvuzecustomitems.listemers.ProjectileHit;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomItems extends JavaPlugin {

    public static CustomItems plugin;

    @Override
    public void onEnable() {
        plugin = this;

        new BlindnessItem(this, "blindnessitem");
        new PoisonedCannonballItem(this, "poisonedcannonballitem");
        new InvulnerabilityItem(this, "invulnerabilityitem");

        getServer().getPluginManager().registerEvents(new ProjectileHit(), this);
        getServer().getPluginManager().registerEvents(new ArrowHit(), this);
        getServer().getPluginManager().registerEvents(new Explosions(), this);
    }

    @Override
    public void onDisable() {
        plugin = null;
    }
}
