package net.deadpvp.lobby.listeners;

import net.deadpvp.lobby.guiManager.guis.CompassPlayerMenu;
import net.deadpvp.lobby.menu.MainMenu;
import net.deadpvp.lobby.server.ServerManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final ServerManager serverManager;
    private final MainMenu mainMenu;

    public PlayerListener(MainMenu mainMenu, ServerManager serverManager){
        this.serverManager = serverManager;
        this.mainMenu = mainMenu;
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.setGameMode (GameMode.SURVIVAL);
        p.getInventory ().clear();
        p.teleport(new Location(p.getWorld (), 0.5, 50, 0.5, 0, 0));

        p.setWalkSpeed ((float) 0.4);
        if ((p.hasPermission ("chat.builder") ||
                p.hasPermission ("chat.modo") ||
                p.hasPermission ("chat.admin") ||
                p.hasPermission ("chat.dev"))) {
            e.setJoinMessage("§c" + p.getName () + " §6vient de rejoindre le lobby!");
            p.getWorld().strikeLightningEffect(p.getLocation());
        }
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack it = e.getItem();
        if (it == null) return;
        if (!e.getAction().equals(Action.PHYSICAL)) {
            if (e.getPlayer().getItemInHand().getType().equals(Material.COMPASS)) {
                e.setCancelled(true);
                CompassPlayerMenu compassPlayerMenu = new CompassPlayerMenu(e.getPlayer(),this.serverManager);
                compassPlayerMenu.openInv();

                player.openInventory(mainMenu.getInventory());
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        e.setQuitMessage("");
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        e.setCancelled(true);
    }
}
