package net.deadpvp.lobby.commands;

import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.menu.MainMenu;
import net.deadpvp.lobby.scoreboard.ScoreboardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UpdateData implements CommandExecutor {
    private final Configuration configuration;
    private final MainMenu mainMenu;
    private final ScoreboardManager scoreboardManager;

    public UpdateData(Configuration configuration, MainMenu mainMenu, ScoreboardManager scoreboardManager) {
        this.configuration = configuration;
        this.mainMenu = mainMenu;
        this.scoreboardManager = scoreboardManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()){
            this.configuration.loadFileData();
            this.scoreboardManager.updateData();
            this.mainMenu.reloadData(this.configuration);
            commandSender.sendMessage("§aLes données viennent d'être mise à jour!");
        }
        return false;
    }
}
