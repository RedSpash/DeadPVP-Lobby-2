package net.deadpvp.lobby.menu.commands;

import org.bukkit.entity.Player;

public class InventoryCommand implements Command {
    private final String action;

    public InventoryCommand(String action) {
        this.action = action;
    }

    @Override
    public void execute(Player p) {
        if(action.equalsIgnoreCase("close")){
            p.closeInventory();
        }
    }
}
