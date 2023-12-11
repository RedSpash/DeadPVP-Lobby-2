package net.deadpvp.lobby.welcome;

import org.bukkit.entity.Player;

public class WelcomeTitle {

    private final String title;
    private final String subTitle;
    private final int fadeIn;
    private final int time;
    private final int fadeOut;

    public WelcomeTitle(String title, String subTitle, int fadeIn, int time, int fadeOut){
        this.title = title;
        this.subTitle = subTitle;
        this.fadeIn= fadeIn;
        this.time = time;
        this.fadeOut = fadeOut;
    }

    public void sendTitle(Player p){
        p.sendTitle(title,subTitle,fadeIn,time,fadeOut);
    }

}
