package net.deadpvp.lobby.scoreboard;

import com.google.errorprone.annotations.Var;
import net.deadpvp.lobby.config.Configuration;
import net.deadpvp.lobby.server.BungeeManager;
import net.deadpvp.lobby.variables.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ScoreboardManager extends BukkitRunnable {

    public static final String SCOREBOARD_KEY = "scoreboard.";
    private String title;
    private final Map<UUID,RedScoreBoard> scoreBoards;
    private final Configuration configuration;
    private final HashMap<Integer,String> lines;
    private final BungeeManager bungeeManager;
    private VariableManager variableManager;

    public ScoreboardManager(Configuration configuration, BungeeManager bungeeManager, VariableManager variableManager) {
        this.scoreBoards = new HashMap<>();
        this.variableManager = variableManager;
        this.lines = new HashMap<>();
        this.bungeeManager = bungeeManager;
        this.configuration = configuration;

        this.reloadData();
    }

    private void reloadData() {
        FileConfiguration fileConfig = this.configuration.getFileConfiguration();
        this.title = fileConfig.getString(SCOREBOARD_KEY +"title");
        this.lines.clear();
        List<String> listeLines = fileConfig.getStringList(SCOREBOARD_KEY+"lines");
        int index = listeLines.size();
        for(String string : listeLines){
            this.lines.put(index,string);
            index = index - 1;
        }
    }


    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p != null) {
                if(!scoreBoards.containsKey(p.getUniqueId())){
                    setScoreboard(p);
                }else{
                    updateScoreBoard(p);
                }
            }
        }
    }

    public void setScoreboard(Player p) {
        RedScoreBoard redScoreBoard = new RedScoreBoard(p,this.title,new ArrayList<>(this.lines.values()), this.variableManager);
        p.setScoreboard(redScoreBoard.getBoard());
        scoreBoards.put(p.getUniqueId(),redScoreBoard);
    }


    private void updateScoreBoard(Player p) {
        if(!scoreBoards.containsKey(p.getUniqueId())){
            setScoreboard(p);
        }else{
            scoreBoards.get(p.getUniqueId()).update();
        }
    }

    public void updateData() {
        this.reloadData();
        for(RedScoreBoard scoreBoard : this.scoreBoards.values()){
            scoreBoard.updateLines(this.lines.values());
            scoreBoard.update();
        }
    }
}
