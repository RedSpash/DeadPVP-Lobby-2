package net.deadpvp.lobby.sql;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLManager {

    private static final boolean SECURITY_BYPASS = true;
    private Connection connection;

    public SQLManager(){
        this.mysqlSetup();
    }

    private void mysqlSetup() {
        String host = "localhost";
        int port = 3306;
        String database = "minecraftrebased";
        String username = "root";
        String password = "";

        try {
            synchronized (this) {
                if (this.getConnection() != null && !this.getConnection().isClosed()) {
                    return;
                }
                this.connection = DriverManager.getConnection("jdbc:mariadb://" + host + ":"+ port + "/" + database, username, password);
                Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "MYSQL CONNECTED");
            }
        } catch (SQLException throwable) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "MYSQL ERROR "+throwable.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isConnected() throws SQLException {
        return this.connection != null && !this.connection.isClosed() || SQLManager.SECURITY_BYPASS;
    }

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
