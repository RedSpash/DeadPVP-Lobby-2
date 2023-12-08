package net.deadpvp.lobby.listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

public class ProtectionListener implements Listener {

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent e){
        Player p = e.getPlayer();

        if(!p.isOp() && p.getGameMode() != GameMode.CREATIVE){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void fallDamage(EntityDamageEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        e.setCancelled (true);
    }

    @EventHandler
    public void onWeather(WeatherChangeEvent e) {e.setCancelled (true);}

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        if(!e.getPlayer ().isOp() || e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled (true);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if(!e.getPlayer ().isOp() || e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled (true);
    }

    @EventHandler
    public void onBreak(PlayerTeleportEvent e){
        if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL || e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL){
            e.setCancelled(true);
        }
    }



}
