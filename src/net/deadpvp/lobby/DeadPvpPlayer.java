package net.deadpvp.lobby;

import net.deadpvp.lobby.sql.SQLManager;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

public class DeadPvpPlayer {

    private final SQLManager sqlManager;
    private String rankName;
    private String rankColor;
    private final UUID uuid;

    public DeadPvpPlayer(Player p, SQLManager sqlManager){
        this.uuid = p.getUniqueId();
        this.sqlManager = sqlManager;

        this.updateData();
    }

    private void updateData() {
        this.rankName = "Chargement...";
        this.rankColor = "§7";
        if(true){
            return;
        }
        try {
            Connection connection = sqlManager.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("SELECT ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String getRankColor() {
        return rankColor;
    }

    public String getRankName() {
        return rankName;
    }
}
