package me.leopold95.melvuzecustomitems.core;


import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {
    public static void playTo(Player player, String soundStr, int volume){
        try{
            player.playSound(player.getLocation(), Sound.valueOf(soundStr), volume, 1f);
        }
        catch (Exception exp){
            Bukkit.getServer().getConsoleSender().sendMessage(exp.getMessage());
        }
    }
}
