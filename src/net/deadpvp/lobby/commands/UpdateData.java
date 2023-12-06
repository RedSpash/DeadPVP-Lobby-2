package net.deadpvp.lobby.commands;

import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.menu.MainMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UpdateData implements CommandExecutor {
    private final Configuration configuration;
    private final MainMenu mainMenu;

    public UpdateData(Configuration configuration, MainMenu mainMenu) {
        this.configuration = configuration;
        this.mainMenu = mainMenu;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()){
            this.configuration.loadFileData();

            this.mainMenu.reloadData(this.configuration);
        }
        return false;
    }
}
