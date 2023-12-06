package net.deadpvp.lobby.menu.commands;

import net.deadpvp.lobby.server.BungeeManager;
import org.bukkit.entity.Player;

public class SendCommand implements Command{

    private final BungeeManager bungeeManager;
    private final String server;

    public SendCommand(BungeeManager bungeeManager,String server) {
        this.bungeeManager = bungeeManager;
        this.server = server;
    }

    @Override
    public void execute(Player p) {
        this.bungeeManager.sendPlayerToServer(p,server);
    }
}
