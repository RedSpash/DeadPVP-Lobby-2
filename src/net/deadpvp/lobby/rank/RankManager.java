package net.deadpvp.lobby.rank;

import net.deadpvp.lobby.sql.SQLManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RankManager {

    private final SQLManager sqlManager;

    private final ArrayList<Rank> ranks;

    public RankManager(SQLManager sqlManager){
        this.ranks =  new ArrayList<>();
        this.sqlManager = sqlManager;

        this.updateData();
    }

    public void updateData() {
        this.ranks.clear();
        try {
            Connection connection = sqlManager.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT permission,color,prefix,power FROM prefix ORDER BY power ASC");

            while (resultSet.next()){
                String permission = resultSet.getString("permission");
                String color = resultSet.getString("color");
                String prefix = resultSet.getString("prefix");
                int power = resultSet.getInt("power");

                this.ranks.add(new Rank(power,permission,prefix,color));
            }
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Rank> getRanks() {
        return ranks;
    }
}
