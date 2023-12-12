package net.deadpvp.lobby.listeners;

import net.deadpvp.lobby.menu.MainMenu;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.InventoryView;

public class InventoryListeners implements Listener {

    private final MainMenu mainMenu;

    public InventoryListeners(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if(e.getWhoClicked() instanceof Player p && (!p.isOp() || p.getGameMode() != GameMode.CREATIVE)){
            e.setCancelled(true);
        }
        InventoryView inventory = e.getView();
        if(inventory.getTitle().equals(this.mainMenu.getTitle())){
            e.setCancelled(true);
            this.mainMenu.eventHandler(e);
        }
    }

    @EventHandler
    public void onInventoryClick(PlayerSwapHandItemsEvent e) {
        Player p = e.getPlayer();
        if((!p.isOp() || p.getGameMode() != GameMode.CREATIVE)){
            e.setCancelled(true);
        }
    }
}
