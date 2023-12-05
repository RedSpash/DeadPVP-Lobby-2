package net.deadpvp.lobby.listeners;

import net.deadpvp.lobby.guiManager.PlayerMenu;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;

public class InventoryListeners implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) throws IOException {
        InventoryHolder holder = e.getClickedInventory().getHolder();

        if(holder instanceof PlayerMenu menu){
            e.setCancelled(true);
            if(e.getCurrentItem() == null) return;
            menu.eventHandler(e);
        }
    }
}
