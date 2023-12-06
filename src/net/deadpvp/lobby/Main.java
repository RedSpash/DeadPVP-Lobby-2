package net.deadpvp.lobby;

import net.deadpvp.lobby.commands.UpdateData;
import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.listeners.InventoryListeners;
import net.deadpvp.lobby.listeners.PlayerListener;
import net.deadpvp.lobby.listeners.ProtectionListener;
import net.deadpvp.lobby.menu.MainMenu;
import net.deadpvp.lobby.scoreboard.ScoreboardManager;
import net.deadpvp.lobby.server.BungeeManager;
import net.deadpvp.lobby.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public class Main extends JavaPlugin {
    private SQLManager sqlManager;
    private BungeeManager bungeeManager;
    private static Main instance;
    private Configuration configuration;
    private MainMenu mainMenu;
    private ScoreboardManager scoreboardManager;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        try{
            this.sqlManager = new SQLManager();
            if(!this.sqlManager.isConnected()){
                Bukkit.getConsoleSender().sendMessage("§cImpossible de connecter la base de donnée !");
                Bukkit.getServer().shutdown();
                return;
            }
            instance = this;

            this.bungeeManager = new BungeeManager();
            this.configuration = new Configuration(this.getDataFolder());
            this.scoreboardManager = new ScoreboardManager(this.configuration);
            this.mainMenu = new MainMenu(this.configuration,this.bungeeManager);

            Bukkit.getPluginManager().registerEvents(new ProtectionListener(),this);
            Bukkit.getPluginManager().registerEvents(new PlayerListener(this.mainMenu),this);
            Bukkit.getPluginManager().registerEvents(new InventoryListeners(mainMenu),this);

            this.mainMenu.runTaskTimer(this, 1L, 20L*60);
            this.scoreboardManager.runTaskTimer(this,1L,20L*2);

            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            this.getCommand("update").setExecutor(new UpdateData(this.configuration,this.mainMenu));
            Bukkit.getServer().setWhitelist(false);
        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage()+" "+e.getLocalizedMessage());
            Bukkit.getConsoleSender().sendMessage("§cUne erreur est survenue: "+e.getMessage());
            Bukkit.getServer().setWhitelist(true);
            Bukkit.getConsoleSender().sendMessage("§cWhiteList activée!");
        }

    }

    @Override
    public void onDisable(){
        this.sqlManager.close();
    }







}
