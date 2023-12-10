package net.deadpvp.lobby.listeners;

import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.menu.MainMenu;
import net.deadpvp.lobby.players.PlayerManager;
import net.deadpvp.lobby.scoreboard.ScoreboardManager;
import net.deadpvp.lobby.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    private final PlayerManager playerManager;
    private Location spawn;
    private final MainMenu mainMenu;
    private final ScoreboardManager scoreboardManager;
    private final Configuration configuration;

    public PlayerListener(Configuration configuration, MainMenu mainMenu, ScoreboardManager scoreboardManager, PlayerManager playerManager){
        this.mainMenu = mainMenu;
        this.configuration = configuration;
        this.updateSpawnLocation();
        this.scoreboardManager = scoreboardManager;
        this.playerManager = playerManager;
    }

    public void updateSpawnLocation() {
        FileConfiguration fileConfiguration = this.configuration.getFileConfiguration();

        this.spawn = new Location(
                Bukkit.getWorld("world"),
                fileConfiguration.getDouble("spawn.x",0.5),
                fileConfiguration.getDouble("spawn.y",0.5),
                fileConfiguration.getDouble("spawn.z",0.5),
                (float) fileConfiguration.getDouble("spawn.yaw",0.5),
                (float) fileConfiguration.getDouble("spawn.pitch",0.5)
        );
    }

    public void setDefaultInventory(Player p){
        p.getInventory().clear();

        ItemStack itemStack = new ItemStackBuilder(Material.RECOVERY_COMPASS).setName("§6Menu des jeux").hideAttributes().hideAttributes().setLore("§7Clique droit pour","§7ouvrir le menu des jeux.").toItemStack();
        p.getInventory().setItem(4,itemStack);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        p.setGameMode (GameMode.SURVIVAL);
        p.getInventory ().clear();
        this.setDefaultInventory(p);
        p.teleport(spawn);
        p.setFoodLevel(20);
        p.setHealth(20);
        this.playerManager.insertNewPlayer(p);
        scoreboardManager.updateScoreBoardShow(p);
        p.setWalkSpeed ((float) 0.4);
        if ((p.hasPermission ("chat.builder") ||
                p.hasPermission ("chat.modo") ||
                p.hasPermission ("chat.admin") ||
                p.hasPermission ("chat.dev"))) {
            e.setJoinMessage("§c" + p.getName () + " §6vient de rejoindre le lobby!");
            p.getWorld().strikeLightningEffect(p.getLocation());
        }else{
            e.setJoinMessage("");
        }
    }

    @EventHandler
    public void switchGameMode(PlayerGameModeChangeEvent e){
        if(e.getNewGameMode() == GameMode.SURVIVAL){
            this.setDefaultInventory(e.getPlayer());
        }
    }

    @EventHandler
    public void foodLost(FoodLevelChangeEvent e){
        e.setCancelled(true);
        e.setFoodLevel(20);
    }

    @EventHandler
    public void expChangeEvent(PlayerExpChangeEvent e){
        e.setAmount(0);
    }

    @EventHandler
    public void expChangeEvent(PlayerLevelChangeEvent e){
        e.getPlayer().setLevel(0);
    }

    @EventHandler
    public void onInteract (PlayerInteractEvent e) {
        Player player = e.getPlayer();
        ItemStack it = e.getItem();
        if(it == null) return;
        if(!e.getAction().equals(Action.PHYSICAL)) {
            if(e.getPlayer().getItemInHand().getType().equals(Material.RECOVERY_COMPASS)) {
                e.setCancelled(true);
                player.openInventory(mainMenu.getInventory());
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        this.playerManager.removePlayer(e.getPlayer());
        e.setQuitMessage("");
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        if(!e.getPlayer().isOp() || e.getPlayer().getGameMode() != GameMode.CREATIVE) e.setCancelled (true);
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent e){
        if(e.getEntity() instanceof  Player p){
            if(!p.isOp() || p.getGameMode() != GameMode.CREATIVE) e.setCancelled (true);
        }else{
            e.setCancelled(true);
        }
    }
}
