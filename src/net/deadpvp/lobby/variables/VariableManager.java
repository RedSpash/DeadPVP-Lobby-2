package net.deadpvp.lobby.variables;

import net.deadpvp.lobby.players.PlayerManager;
import net.deadpvp.lobby.server.BungeeManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableManager {

    private final BungeeManager bungeeManager;
    private final PlayerManager playerManager;

    public VariableManager(BungeeManager bungeeManager, PlayerManager playerManager){
        this.bungeeManager = bungeeManager;
        this.playerManager = playerManager;
    }

    public String getStringWithReplacedVariables(String text, Player p){
        // {player} -> nom du joueur
        // {all.playercount} -> nombre de joueurs
        // {rank} -> nom du rank
        // {rankcolor} -> couleur du rank
        List<String> variables = this.getVariablesFromText(text);
        String correctedText = text;

        for(String variable : variables){
            String correctedVariable = variable;
            String replaceBy = "UNKNOW";
            if(correctedVariable.startsWith("§")){
                correctedVariable = correctedVariable.substring(2);
            }
            if(correctedVariable.contains(".")){
                String server = correctedVariable.split("\\.")[0];
                String action = correctedVariable.split("\\.")[1];
                if(action.equalsIgnoreCase("playercount")){
                    replaceBy = String.valueOf(this.bungeeManager.getConnectedPlayer(server));
                } else if (action.equalsIgnoreCase("status")) {
                    replaceBy = String.valueOf(this.bungeeManager.getStatus(server));
                }
            }else if(correctedVariable.startsWith("0x")){
                replaceBy = ChatColor.of(Color.decode(correctedVariable))+"";
            }else if(p != null){
                if (correctedVariable.equalsIgnoreCase("name") || correctedVariable.equalsIgnoreCase("player")) {
                    replaceBy = p.getName();
                }else if (correctedVariable.equalsIgnoreCase("rankcolor")) {
                    replaceBy = this.playerManager.getData(p.getUniqueId()).getRankColor();
                }else if (correctedVariable.equalsIgnoreCase("rank")) {
                    replaceBy = this.playerManager.getData(p.getUniqueId()).getRankName();
                }
            }
            correctedText = correctedText.replace("{"+variable+"}",replaceBy);

        }
        return correctedText;
    }

    private List<String> getVariablesFromText(String text) {
        List<String> result = new ArrayList<>();

        Pattern pattern = Pattern.compile("\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            result.add(matcher.group(1));
        }

        return result;
    }

    public String getStringWithReplacedVariables(String line) {
        return getStringWithReplacedVariables(line,null);
    }
}
