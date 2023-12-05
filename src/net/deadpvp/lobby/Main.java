package net.deadpvp.lobby;

import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.listeners.InventoryListeners;
import net.deadpvp.lobby.listeners.PlayerListener;
import net.deadpvp.lobby.listeners.ProtectionListener;
import net.deadpvp.lobby.menu.MainMenu;
import net.deadpvp.lobby.server.ServerManager;
import net.deadpvp.lobby.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private SQLManager sqlManager;
    private ServerManager serverManager;
    private static Main instance;
    private Configuration configuration;
    private MainMenu mainMenu;

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

            this.serverManager = new ServerManager();
            this.configuration = new Configuration(this.getDataFolder());
            this.mainMenu = new MainMenu(this.configuration);

            Bukkit.getPluginManager().registerEvents(new ProtectionListener(),this);
            Bukkit.getPluginManager().registerEvents(new PlayerListener(this.mainMenu,this.serverManager),this);
            Bukkit.getPluginManager().registerEvents(new InventoryListeners(),this);

            this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
            Bukkit.getServer().setWhitelist(false);
        }catch (Exception e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage()+" "+e.getLocalizedMessage());
            Bukkit.getConsoleSender().sendMessage("§cUne erreur est survenue: "+e.getMessage());
            Bukkit.getServer().setWhitelist(true);
            Bukkit.getConsoleSender().sendMessage("§cWhiteList activée!");
        }

    }







}
