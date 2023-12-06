package net.deadpvp.lobby.server.tasks;

import com.google.common.collect.Iterables;
import net.deadpvp.lobby.server.BungeeManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class updatePlayerCount implements Runnable {

    private final BungeeManager bungeeManager;

    public updatePlayerCount(BungeeManager bungeeManager) {
        this.bungeeManager = bungeeManager;
    }

    @Override
    public void run() {
        this.bungeeManager.updatePlayerCount();
    }
}
