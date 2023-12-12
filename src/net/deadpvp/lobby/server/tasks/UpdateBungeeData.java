package net.deadpvp.lobby.server.tasks;

import net.deadpvp.lobby.server.BungeeManager;
import org.bukkit.scheduler.BukkitRunnable;

public class UpdateBungeeData extends BukkitRunnable {

    private final BungeeManager bungeeManager;

    public UpdateBungeeData(BungeeManager bungeeManager) {
        this.bungeeManager = bungeeManager;
    }

    @Override
    public void run() {
        this.bungeeManager.updatePlayerCount();
        this.bungeeManager.updateServersStatus();
    }
}
