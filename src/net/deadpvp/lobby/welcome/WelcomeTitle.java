package net.deadpvp.lobby.welcome;

import net.deadpvp.lobby.variables.VariableManager;
import org.bukkit.entity.Player;

public class WelcomeTitle {

    private final String title;
    private final String subTitle;
    private final int fadeIn;
    private final int time;
    private final int fadeOut;
    private final VariableManager variableManager;

    public WelcomeTitle(VariableManager variableManager, String title, String subTitle, int fadeIn, int time, int fadeOut){
        this.variableManager = variableManager;
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn= fadeIn;
        this.time = time;
        this.fadeOut = fadeOut;
    }

    public void sendTitle(Player p){
        p.sendTitle(
                this.variableManager.getStringWithReplacedVariables(title,p),
                this.variableManager.getStringWithReplacedVariables(subTitle,p),
                fadeIn,
                time,
                fadeOut
        );
    }

}
