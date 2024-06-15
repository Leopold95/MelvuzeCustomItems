package me.leopold95.melvuzecustomitems.core;


import org.bukkit.Bukkit;
import org.bukkit.Location;
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

    public static void playOn(Location location, String soundStr, int volume, int soundRadius){
        try{
            for(Player player : location.getNearbyEntitiesByType(Player.class, soundRadius)){
                playTo(player,soundStr, volume);
            }
        }
        catch (Exception exp){
            Bukkit.getServer().getConsoleSender().sendMessage(exp.getMessage());
        }
    }
}
