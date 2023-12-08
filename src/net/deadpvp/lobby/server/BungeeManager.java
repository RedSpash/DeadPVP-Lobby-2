package net.deadpvp.lobby.server;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.deadpvp.lobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class BungeeManager implements PluginMessageListener {

    public static final String BUNGEE_CORD_CHANNEL = "BungeeCord";
    private final HashMap<String,Integer> serverPlayerCount;
    public BungeeManager(){
        this.serverPlayerCount = new HashMap<>();
    }

    public void sendPlayerToServer(Player p, String server) {
        try{
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            p.sendPluginMessage(Main.getInstance(), BUNGEE_CORD_CHANNEL, b.toByteArray());
            b.close();
            out.close();
        }catch (IOException exception){
            Bukkit.getConsoleSender().sendMessage("§c"+exception.getMessage());
        }

    }

    public void updateServerList(Player p) {
        try{
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("GetServers");
            p.sendPluginMessage(Main.getInstance(), BUNGEE_CORD_CHANNEL, b.toByteArray());
            b.close();
            out.close();
        }catch (IOException exception){
            Bukkit.getConsoleSender().sendMessage("§c"+exception.getMessage());
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player,byte[] message) {
        if (!channel.equals(BUNGEE_CORD_CHANNEL)) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playerCount = in.readInt();
            this.serverPlayerCount.put(server,playerCount);
        } else if (subchannel.equals("GetServers")) {
            String[] serverList = in.readUTF().split(", ");
            for(String serverName : serverList){
                this.serverPlayerCount.putIfAbsent(serverName,0);
            }
        }
    }

    public int getConnectedPlayer(String server){
        if(server.equalsIgnoreCase("all")){
            int result = 0;
            for(int value : this.serverPlayerCount.values()){
                result = result + value;
            }
            return result;
        }
        return this.serverPlayerCount.getOrDefault(server, 0);
    }

    public void updatePlayerCount() {
        if(!Bukkit.getOnlinePlayers().isEmpty()){
            Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
            this.updateServerList(player);
        }
    }
}
