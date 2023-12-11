package net.deadpvp.lobby;

import net.deadpvp.lobby.commands.UpdateData;
import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.listeners.InventoryListeners;
import net.deadpvp.lobby.listeners.PlayerListener;
import net.deadpvp.lobby.listeners.ProtectionListener;
import net.deadpvp.lobby.menu.MainMenu;
import net.deadpvp.lobby.players.PlayerManager;
import net.deadpvp.lobby.rank.RankManager;
import net.deadpvp.lobby.scoreboard.ScoreboardManager;
import net.deadpvp.lobby.server.BungeeManager;
import net.deadpvp.lobby.sql.SQLManager;
import net.deadpvp.lobby.variables.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {
    private SQLManager sqlManager;
    private BungeeManager bungeeManager;
    private static Main instance;
    private Configuration configuration;
    private MainMenu mainMenu;
    private ScoreboardManager scoreboardManager;
    private PlayerManager playerManager;
    private VariableManager variableManager;
    private PlayerListener playerListener;
    private RankManager rankManager;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Main.instance = this;
        try{
            this.sqlManager = new SQLManager();
            if(!this.sqlManager.isConnected()){
                Bukkit.getConsoleSender().sendMessage("§cImpossible de connecter la base de donnée !");
                Bukkit.getServer().setWhitelist(true);
                //return;
            }

            this.rankManager = new RankManager(this.sqlManager);
            this.playerManager = new PlayerManager(this.rankManager);
            this.bungeeManager = new BungeeManager();
            this.configuration = new Configuration(this.getDataFolder());
            this.variableManager = new VariableManager(this.bungeeManager,this.playerManager);
            this.scoreboardManager = new ScoreboardManager(this.configuration,this.bungeeManager,this.variableManager);
            this.mainMenu = new MainMenu(this.configuration,this.bungeeManager,this.variableManager);

            this.playerListener = new PlayerListener(this.configuration,this.mainMenu,this.scoreboardManager,this.playerManager);
            Bukkit.getPluginManager().registerEvents(new ProtectionListener(),this);
            Bukkit.getPluginManager().registerEvents(this.playerListener,this);
            Bukkit.getPluginManager().registerEvents(new InventoryListeners(mainMenu),this);

            this.mainMenu.runTaskTimer(this, 1L, 20L*60);
            this.scoreboardManager.runTaskTimer(this,1L,20L*2);

            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord",this.bungeeManager);

            this.getCommand("update").setExecutor(new UpdateData(this.configuration,this.playerListener, this.mainMenu,this.scoreboardManager, this.rankManager, this.playerManager));

            Bukkit.getServer().setWhitelist(false);

            this.initialiseOnlinePlayers();

        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage()+" "+e.getLocalizedMessage());
            Bukkit.getConsoleSender().sendMessage("§cUne erreur est survenue: "+e.getMessage());
            Bukkit.getServer().setWhitelist(true);
            Bukkit.getConsoleSender().sendMessage("§cWhiteList activée!");
            e.printStackTrace();
        }
    }

    private void initialiseOnlinePlayers() {
        for(Player p : Bukkit.getOnlinePlayers()){
            this.playerManager.insertNewPlayer(p);
        }
    }

    @Override
    public void onDisable(){
        this.sqlManager.close();
    }







}
