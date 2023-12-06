package net.deadpvp.lobby;

import net.deadpvp.lobby.sql.SQLManager;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DeadPvpPlayer {

    private final SQLManager sqlManager;
    private String rankName;
    private String rankColor;

    public DeadPvpPlayer(Player p, SQLManager sqlManager){
        this.updateData();
        this.sqlManager = sqlManager;
    }

    private void updateData() {
        try {
            Connection connection = sqlManager.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("SELECT ");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
