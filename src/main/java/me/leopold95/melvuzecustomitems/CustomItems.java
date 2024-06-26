package me.leopold95.melvuzecustomitems;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import me.leopold95.melvuzecustomitems.core.Config;
import me.leopold95.melvuzecustomitems.core.SkinManager;
import me.leopold95.melvuzecustomitems.core.animations.AnimationWorker;
import me.leopold95.melvuzecustomitems.items.*;
import me.leopold95.melvuzecustomitems.listeners.PlayerJoin;
import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.SkinsRestorerProvider;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomItems extends JavaPlugin {

    public static CustomItems plugin;

    public ICombatLogX combatLogX;
    public SkinsRestorer skinsRestorer;
    public SkinManager skinManager;

    private AnimationWorker animationWorker;


    @Override
    public void onEnable() {
        plugin = this;

        Config.register(this);

        combatLogX = getAPI();
        skinsRestorer = SkinsRestorerProvider.get();
        skinManager = new SkinManager(this);

        animationWorker = new AnimationWorker(this, Config.getInt("animation-update-ticks"));

        new BlindnessItem(this, "blindness_item");
        new PoisonedCannonballItem(this, "poisonedcannonball_item");
        new InvulnerabilityItem(this, "invulnerability_item");
        new ShieldItem(this, "shield_item");
        new CobwebSlow(this, "cobweb_slow_item");
        new InfectionItem(this, "infection_item");

        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);

        animationWorker.run();
    }

    private ICombatLogX getAPI() {
        PluginManager pluginManager = Bukkit.getPluginManager();
        Plugin plugin = pluginManager.getPlugin("CombatLogX");
        return (ICombatLogX) plugin;
    }

    @Override
    public void onDisable() {
        plugin = null;
    }
}
