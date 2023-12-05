package net.deadpvp.lobby.server;

import net.deadpvp.lobby.Main;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class ServerManager {

    public ServerManager(){

    }

    public void sendPlayerToServer(Player p, String server) {
        try{
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            p.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }catch (IOException exception){
            exception.printStackTrace();
        }

    }

}
