package net.deadpvp.lobby.sql;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager {

    private Connection connection;

    public SQLManager(){
        this.mysqlSetup();
    }

    private void mysqlSetup() {
        String host = "localhost";
        int port = 3306;
        String database = "DEADPVP";
        String username = "root";
        String password = "KJ7%td?byEkRXc68#Q";

        try {
            synchronized (this) {
                if (this.getConnection() != null && !this.getConnection().isClosed()) {
                    return;
                }
                this.connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database, username,password);
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
            }
        } catch (SQLException throwable) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "MYSQL ERROR "+throwable.getMessage());
            throwable.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() throws SQLException {
        return this.connection != null && !this.connection.isClosed();
    }

    public void close() {
        try {
            if(this.connection != null){
                this.connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
