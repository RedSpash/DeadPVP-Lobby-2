package net.deadpvp.lobby.server.tasks;

import net.deadpvp.lobby.server.BungeeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdatePlayerCount extends BukkitRunnable {

    private final BungeeManager bungeeManager;

    public UpdatePlayerCount(BungeeManager bungeeManager) {
        this.bungeeManager = bungeeManager;
    }

    @Override
    public void run() {
        this.bungeeManager.updatePlayerCount();
    }
}
