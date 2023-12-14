package net.deadpvp.lobby;

import net.deadpvp.lobby.rank.Rank;
import net.deadpvp.lobby.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeadPvpPlayer {

    private final RankManager rankManager;
    private final UUID uuid;
    private Rank rank;

    public DeadPvpPlayer(UUID uuid, RankManager rankManager){
        this.uuid = uuid;
        this.rankManager = rankManager;

        this.updateData();
    }

    public void updateData() {
        Player p = Bukkit.getPlayer(this.uuid);

        if(p != null){
            for(Rank rank : this.rankManager.getRanks()){
                if(rank.hasPermission(p)){
                    this.rank = rank;
                    break;
                }
            }
        }
    }

    public String getRankColor() {
        return this.rank.getColor();
    }

    public String getRankName() {
        return this.rank.getPrefix();
    }

    public String getLongRankName() {
        return this.rank.getLongName();
    }

    public UUID getUuid() {
        return uuid;
    }
}
