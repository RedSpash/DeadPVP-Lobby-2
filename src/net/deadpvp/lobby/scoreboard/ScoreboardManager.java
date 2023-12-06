package net.deadpvp.lobby.scoreboard;

import net.deadpvp.lobby.config.Configuration;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager extends BukkitRunnable {

    public static final String SCOREBOARD_KEY = "scoreboard.";
    private String title;
    private final Map<UUID,RedScoreBoard> scoreBoards;
    private final Configuration configuration;
    private final HashMap<Integer,String> lines;

    public ScoreboardManager(Configuration configuration) {
        this.scoreBoards = new HashMap<>();
        this.lines = new HashMap<>();
        this.configuration = configuration;

        this.reloadData();
    }

    private void reloadData() {
        FileConfiguration fileConfig = this.configuration.getFileConfiguration();
        this.title = fileConfig.getString(SCOREBOARD_KEY +"title");
        this.lines.clear();

        fileConfig.getConfigurationSection(SCOREBOARD_KEY+"lines").getKeys(false).forEach(lineNumber -> {
            this.lines.put(
                    Integer.parseInt(lineNumber),
                    fileConfig.getString(SCOREBOARD_KEY +"lines."+lineNumber)
            );
        });
    }


    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            if(p != null) {
                if(!scoreBoards.containsKey(p.getUniqueId())){
                    setScoreboard(p);
                    updateTeams(p);
                }else{
                    updateScoreBoard(p);
                }
            }
        }
    }

    public void updateTeams(Player p){
        RedScoreBoard board = scoreBoards.get(p.getUniqueId());
        if(board == null){
            setScoreboard(p);
            board = scoreBoards.get(p.getUniqueId());
        }
        ArrayList<String> hasTeam = new ArrayList<>();
        for(Player pl : Bukkit.getOnlinePlayers()){
            if(!hasTeam.contains(pl.getName())){
                Team team =null;
                if(pl.isOp()){
                    team = board.getTeam("dev");
                    if(team == null){
                        team = board.createTeam("dev");
                        team.setPrefix("§c§lDEV - ");
                    }
                }else{
                    team = board.getTeam("noTeam");
                    if(team == null){
                        team = board.createTeam("noTeam");
                        team.setPrefix("§7");
                    }
                }
                if(!team.getEntries().contains(pl.getName())){
                    team.addEntry(pl.getName());
                }
            }

        }
    }

    public void setScoreboard(Player p) {
        RedScoreBoard redScoreBoard = new RedScoreBoard(p,"§c§lBEDWARS");
        p.setScoreboard(redScoreBoard.getBoard());
        scoreBoards.put(p.getUniqueId(),redScoreBoard);
    }


    private void updateScoreBoard(Player p) {
        if(!scoreBoards.containsKey(p.getUniqueId())){
            setScoreboard(p);
        }
        RedScoreBoard board = scoreBoards.get(p.getUniqueId());
        board.setLine(4,"§5§l▪ PROFIL");
        board.setLine(3,"§f≫ Pseudo: §b"+p.getName());
        board.setLine(2,"§f≫ Grade: §b"+"????");
        board.setLine(1,"§5");
        board.setLine(0,"§7deadpvp.net");
    }
}
