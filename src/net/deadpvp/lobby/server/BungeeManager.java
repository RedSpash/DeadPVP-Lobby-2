package net.deadpvp.lobby.server;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.deadpvp.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class BungeeManager implements PluginMessageListener {

    public static final String BUNGEE_CORD_CHANNEL = "BungeeCord";
    public static final String DEADPVP_CHANNEL = "deadpvp:server_status";

    private static final String OFFLINE_MESSAGE = "§cHors ligne";
    private static final String ONLINE_MESSAGE = "§aEn ligne";
    private static final String WAITING_MESSAGE = "§7Vérification en cours...";
    private final HashMap<String,BungeeServer> servers;

    public BungeeManager(){
        this.servers = new HashMap<>();
    }

    private void sendMessageChannel(Player p,String mainChannel, String subChannel, String message){
        try{
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF(subChannel);
            if(message != null){
                out.writeUTF(message);
            }
            p.sendPluginMessage(Main.getInstance(), mainChannel, b.toByteArray());
            b.close();
            out.close();
        }catch (IOException exception){
            Bukkit.getConsoleSender().sendMessage("§c"+exception.getMessage());
        }
    }

    private void sendMessageChannel(Player p,String mainChannel, String subChannel) {
        this.sendMessageChannel(p,mainChannel,subChannel,null);
    }

    public void sendPlayerToServer(Player p, String server) {
        this.sendMessageChannel(p,BUNGEE_CORD_CHANNEL,"Connect",server);
    }

    public void updateServerList(Player p) {
        this.sendMessageChannel(p,BUNGEE_CORD_CHANNEL,"GetServers");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player,byte[] message) {
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();
        if (!channel.equals(BUNGEE_CORD_CHANNEL)
                && !channel.equals(DEADPVP_CHANNEL)) return;

        switch (subChannel) {
            case "PlayerCount" -> {
                String server = in.readUTF();
                int playerCount = in.readInt();
                if (this.servers.containsKey(server)) {
                    BungeeServer bungeeServer = this.servers.get(server);
                    bungeeServer.setPlayers(playerCount);
                } else {
                    this.servers.put(server, new BungeeServer(playerCount, Math.min(playerCount,1)));
                }
            }
            case "GetServers" -> {
                String[] serverList = in.readUTF().split(", ");
                for (String serverName : serverList) {
                    this.sendMessageChannel(player, BUNGEE_CORD_CHANNEL,"PlayerCount", serverName);
                    this.servers.putIfAbsent(serverName, new BungeeServer(0, -1));
                }
            }
            case "ServerStatusAnswer" -> {
                String server = in.readUTF();
                int status = in.readInt();
                if (this.servers.containsKey(server)) {
                    BungeeServer bungeeServer = this.servers.get(server);
                    bungeeServer.setStatus(status);
                } else {
                    this.servers.put(server, new BungeeServer(0, status));
                }
            }
        }
    }

    public int getConnectedPlayer(String server){
        if(server.equalsIgnoreCase("all")){
            int result = 0;
            for(BungeeServer bungeeServer : this.servers.values()){
                result = result + bungeeServer.getPlayers();
            }

            return result;
        }
        return this.servers.getOrDefault(server, new BungeeServer(0,0)).getPlayers();
    }

    public void updatePlayerCount() {
        if(!Bukkit.getOnlinePlayers().isEmpty()){
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            this.updateServerList(player);
        }
    }

    public String getStatus(String server){
        if(this.servers.containsKey(server)){
            int value = this.servers.get(server).getStatus();
            if(value == 1){
                return ONLINE_MESSAGE;
            }else if(value == 0){
                return OFFLINE_MESSAGE;
            }

        }
        return WAITING_MESSAGE;
    }

    public void updateServersStatus() {
        if(!Bukkit.getOnlinePlayers().isEmpty()){
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            for(String server: this.servers.keySet()){
                this.sendMessageChannel(player,DEADPVP_CHANNEL,"ServerStatus",server);
            }
        }
    }
}
