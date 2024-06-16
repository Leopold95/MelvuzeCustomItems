package me.leopold95.melvuzecustomitems;

import com.github.sirblobman.combatlogx.api.ICombatLogX;
import me.leopold95.melvuzecustomitems.items.BlindnessItem;
import me.leopold95.melvuzecustomitems.items.InvulnerabilityItem;
import me.leopold95.melvuzecustomitems.items.PoisonedCannonballItem;
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


    @Override
    public void onEnable() {
        plugin = this;

        combatLogX = getAPI();
        skinsRestorer = SkinsRestorerProvider.get();

        new BlindnessItem(this, "blindnessitem");
        new PoisonedCannonballItem(this, "poisonedcannonballitem");
        new InvulnerabilityItem(this, "invulnerabilityitem");
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
