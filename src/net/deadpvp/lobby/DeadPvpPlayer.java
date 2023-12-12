package net.deadpvp.lobby;

import net.deadpvp.lobby.rank.Rank;
import net.deadpvp.lobby.rank.RankManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DeadPvpPlayer {

    private final RankManager rankManager;
    private String rankName;
    private String rankColor;
    private final UUID uuid;

    public DeadPvpPlayer(Player p, RankManager rankManager){
        this.uuid = p.getUniqueId();
        this.rankManager = rankManager;

        this.updateData();
    }

    public void updateData() {
        Player p = Bukkit.getPlayer(this.uuid);

        this.rankName = "Chargement...";
        this.rankColor = "§7";

        if(p != null){
            for(Rank rank : this.rankManager.getRanks()){
                if(rank.hasPermission(p)){
                    this.rankName = rank.getLongName();
                    this.rankColor = rank.getColor();
                    break;
                }
            }
        }
    }

    public String getRankColor() {
        return rankColor;
    }

    public String getRankName() {
        return rankName;
    }
}
