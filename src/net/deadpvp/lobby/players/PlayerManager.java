package net.deadpvp.lobby.players;

import net.deadpvp.lobby.DeadPvpPlayer;
import net.deadpvp.lobby.rank.RankManager;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager {

    private final HashMap<UUID,DeadPvpPlayer> playerData;
    private RankManager rankManager;

    public PlayerManager(RankManager rankManager){
        this.playerData = new HashMap<>();
        this.rankManager = rankManager;
    }

    public void insertNewPlayer(Player p){
        this.insertNewPlayer(p.getUniqueId());
    }

    public void insertNewPlayer(UUID uuid){
        this.playerData.put(uuid,new DeadPvpPlayer(uuid,this.rankManager));
    }

    public DeadPvpPlayer getData(UUID uuid){
        if(!this.playerData.containsKey(uuid)) {
            this.insertNewPlayer(uuid);
        }
        return this.playerData.getOrDefault(uuid,null);
    }

    public void removePlayer(Player player) {
        this.playerData.remove(player.getUniqueId());
    }

    public void updateData() {
        for(DeadPvpPlayer deadPvpPlayer : this.playerData.values()){
            deadPvpPlayer.updateData();
        }
    }
}
