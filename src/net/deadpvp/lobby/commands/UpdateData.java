package net.deadpvp.lobby.commands;

import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.listeners.PlayerListener;
import net.deadpvp.lobby.menu.MainMenu;
import net.deadpvp.lobby.players.PlayerManager;
import net.deadpvp.lobby.rank.RankManager;
import net.deadpvp.lobby.scoreboard.ScoreboardManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UpdateData implements CommandExecutor {
    private final Configuration configuration;
    private final MainMenu mainMenu;
    private final ScoreboardManager scoreboardManager;
    private final PlayerListener playerListener;
    private final RankManager rankManager;
    private final PlayerManager playerManager;

    public UpdateData(Configuration configuration, PlayerListener playerListener, MainMenu mainMenu, ScoreboardManager scoreboardManager, RankManager rankManager, PlayerManager playerManager) {
        this.configuration = configuration;
        this.mainMenu = mainMenu;
        this.playerListener = playerListener;
        this.scoreboardManager = scoreboardManager;
        this.rankManager = rankManager;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender.isOp()){
            this.configuration.loadFileData();
            this.scoreboardManager.updateData();
            this.playerListener.updateSpawnLocation();
            this.playerListener.updateWelcomeTitle();
            this.rankManager.updateData();
            this.playerManager.updateData();
            this.mainMenu.reloadData(this.configuration);
            commandSender.sendMessage("§aLes données viennent d'être mise à jour!");
        }
        return false;
    }
}
