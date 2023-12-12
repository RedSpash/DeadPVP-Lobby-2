package net.deadpvp.lobby.menu.commands;

import org.bukkit.entity.Player;

public class SendMessageCommand implements Command{

    private final String message;

    public SendMessageCommand(String message) {
        this.message = message;
    }

    @Override
    public void execute(Player p) {
        p.sendMessage(message);
    }
}
