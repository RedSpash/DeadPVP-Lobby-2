package net.deadpvp.lobby.listeners;

import net.deadpvp.lobby.DeadPvpPlayer;
import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.menu.MainMenu;
import net.deadpvp.lobby.players.PlayerManager;
import net.deadpvp.lobby.rank.RankManager;
import net.deadpvp.lobby.scoreboard.ScoreboardManager;
import net.deadpvp.lobby.utils.ItemStackBuilder;
import net.deadpvp.lobby.variables.VariableManager;
import net.deadpvp.lobby.welcome.WelcomeTitle;
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
    private final MainMenu mainMenu;
    private final ScoreboardManager scoreboardManager;
    private final Configuration configuration;
    private final VariableManager variableManager;
    private final RankManager rankManager;
    private WelcomeTitle welcomeTitle;
    private Location spawn;

    public PlayerListener(VariableManager variableManager, RankManager rankManager, Configuration configuration, MainMenu mainMenu, ScoreboardManager scoreboardManager, PlayerManager playerManager){
        this.variableManager = variableManager;
        this.mainMenu = mainMenu;
        this.configuration = configuration;
        this.updateSpawnLocation();
        this.scoreboardManager = scoreboardManager;
        this.playerManager = playerManager;
        this.rankManager =rankManager;

        this.updateWelcomeTitle();
    }

    public void updateWelcomeTitle() {
        FileConfiguration fileConfiguration = this.configuration.getFileConfiguration();
        String path = "title.";
        this.welcomeTitle = new WelcomeTitle(
                this.variableManager,
                fileConfiguration.getString(path+"title","§f"),
                fileConfiguration.getString(path+"subtitle","§f"),
                fileConfiguration.getInt(path+"fade.in",0),
                fileConfiguration.getInt(path+"fade.time",0),
                fileConfiguration.getInt(path+"fade.out",0)
        );
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
        this.welcomeTitle.sendTitle(p);
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
            DeadPvpPlayer deadPvpPlayer = this.playerManager.getData(p.getUniqueId());
            e.setJoinMessage(this.variableManager.replacePlayerVariables(this.rankManager.getRankFormat(),p)+" "+deadPvpPlayer.getRankColor() + p.getName () + " §6vient de rejoindre le lobby!");
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
    public void onInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(player.getGameMode() != GameMode.CREATIVE || !player.hasPermission("deadpvp.staff.dev")){
            e.setCancelled(true);
        }
        ItemStack item = e.getItem();
        if(item == null) return;
        if((e.getAction().equals(Action.RIGHT_CLICK_AIR) ||
                e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) &&
                        item.getType().equals(Material.RECOVERY_COMPASS)) {
                player.openInventory(mainMenu.getInventory(player));

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
