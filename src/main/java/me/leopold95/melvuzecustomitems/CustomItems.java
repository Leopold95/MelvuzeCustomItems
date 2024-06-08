package me.leopold95.melvuzecustomitems;

import items.BlindnessItem;
import items.PoisonedCannonballItem;
import listemers.ArrowHit;
import listemers.Explosions;
import listemers.ProjectileHit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomItems extends JavaPlugin {

    public static CustomItems plugin;

    @Override
    public void onEnable() {
        plugin = this;

        new BlindnessItem(this, "blindnessitem");
        new PoisonedCannonballItem(this, "poisonedcannonballitem");

        getServer().getPluginManager().registerEvents(new ProjectileHit(), this);
        getServer().getPluginManager().registerEvents(new ArrowHit(), this);
        getServer().getPluginManager().registerEvents(new Explosions(), this);
    }

    @Override
    public void onDisable() {
        plugin = null;
    }
}
