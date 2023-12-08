package net.deadpvp.lobby.scoreboard;

import net.deadpvp.lobby.variables.VariableManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.*;

public class RedScoreBoard {

    private final HashMap<Integer,Team> lines;
    private final Scoreboard board;
    private final Objective objective;
    private static final ArrayList<ChatColor> chatColors = new ArrayList<>(Arrays.asList(ChatColor.values()));
    private final VariableManager variableManager;
    private ArrayList<String> lineText;
    private final UUID uuid;

    public RedScoreBoard(Player p, String title,ArrayList<String> lineText, VariableManager variableManager){
        this.variableManager = variableManager;
        this.lineText = lineText;
        this.lines = new HashMap<>();
        this.board = Bukkit.getScoreboardManager().getNewScoreboard();
        this.uuid = p.getUniqueId();
        this.objective = board.registerNewObjective(title,"dummy");
        this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        this.update();
    }

    public Team getTeam(String name){
        return this.board.getTeam(name);
    }

    public Team createTeam(String name){
        return this.board.registerNewTeam(name);
    }


    public void setLine(Integer position, String text){
        if(!lines.containsKey(position)){
            this.createTeam(position,text);
        }else{
            this.setTeamName(lines.get(position),text,position);
        }
    }

    private void createTeam(Integer position,String text) {
        Team team = board.registerNewTeam(chatColors.get(position)+"");

        team.addEntry(chatColors.get(position)+"");
        this.objective.getScore(chatColors.get(position)+"").setScore(position);

        setTeamName(team,text,position);

        lines.put(position,team);
    }

    private void setTeamName(Team team, String text, Integer position) {
        team.setPrefix(text);
    }

    public Scoreboard getBoard() {
        return board;
    }

    public void update() {
        Player p = Bukkit.getPlayer(this.uuid);
        if(p != null && p.isOnline()){
            int index = 0;
            for(String line : this.lineText){
                this.setLine(index,this.variableManager.getStringWithReplacedVariables(line, p));
                index = index +1;
            }
        }
    }

    public void updateLines(Collection<String> lines) {
        this.lineText = new ArrayList<>(lines);
        this.update();
    }
}