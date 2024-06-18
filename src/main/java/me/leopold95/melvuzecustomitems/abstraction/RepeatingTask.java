package me.leopold95.melvuzecustomitems.abstraction;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public abstract class RepeatingTask implements Runnable {
    private int taskId;

    public RepeatingTask(Plugin plugin, int startIn, int taskTickDelay) {
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, startIn, taskTickDelay);
    }

    public void canncel() {
        Bukkit.getScheduler().cancelTask(taskId);
    }
}