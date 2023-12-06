package net.deadpvp.lobby.scoreboard;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RedScoreBoard {

    private HashMap<Integer,Team> Lines = new HashMap<>();
    private Scoreboard board;
    private UUID playerUUID;
    private String title;
    private Objective objective;

    private static ArrayList<ChatColor> chatColors = new ArrayList<>(Arrays.asList(ChatColor.values()));

    public RedScoreBoard(Player p, String title){
        this.playerUUID = p.getUniqueId();
        this.title = title;
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        this.objective = board.registerNewObjective(title,"dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
    }

    public Team getTeam(String name){
        return this.board.getTeam(name);
    }

    public Team createTeam(String name){
        return this.board.registerNewTeam(name);
    }


    public void setLine(Integer position, String text){
        if(!Lines.containsKey(position)){
            this.createTeam(position,text);
        }else{
            this.setTeamName(Lines.get(position),text,position);
        }
    }

    private void createTeam(Integer position,String text) {
        Team team = board.registerNewTeam(chatColors.get(position)+"");

        team.addEntry(chatColors.get(position)+"");
        this.objective.getScore(chatColors.get(position)+"").setScore(position);

        setTeamName(team,text,position);

        Lines.put(position,team);
    }

    private void setTeamName(Team team, String text, Integer position) {
        team.setPrefix(text);
    }

    public Scoreboard getBoard() {
        return board;
    }

}