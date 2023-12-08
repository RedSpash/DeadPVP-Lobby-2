package net.deadpvp.lobby.players;

import net.deadpvp.lobby.DeadPvpPlayer;
import net.deadpvp.lobby.sql.SQLManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private final HashMap<UUID,DeadPvpPlayer> playerData;
    private final SQLManager sqlManager;

    public PlayerManager(SQLManager sqlManager){
        this.playerData = new HashMap<>();
        this.sqlManager = sqlManager;
    }

    public void insertNewPlayer(Player p){
        this.playerData.put(p.getUniqueId(),new DeadPvpPlayer(p,this.sqlManager));
    }

    public DeadPvpPlayer getData(UUID uuid){
        return this.playerData.getOrDefault(uuid,null);
    }

}
