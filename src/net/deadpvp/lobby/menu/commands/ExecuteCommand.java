package net.deadpvp.lobby.menu.commands;

import org.bukkit.entity.Player;

public class ExecuteCommand implements Command{

    private final String command;

    public ExecuteCommand(String command) {
        this.command = command;
    }


    @Override
    public void execute(Player p) {
        p.performCommand(this.command);
    }
}
