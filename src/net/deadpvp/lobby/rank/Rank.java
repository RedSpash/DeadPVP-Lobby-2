package net.deadpvp.lobby.rank;

import org.bukkit.entity.Player;

public class Rank {

    private final int power;
    private final String permission;
    private final String prefix;
    private final String longName;
    private final String color;

    public Rank(int power, String permission, String prefix, String longName, String color) {
        this.power = power;
        this.permission = permission;
        this.longName = longName;
        this.prefix = prefix;
        this.color = color;
    }

    public int getPower() {
        return power;
    }

    public String getColor() {
        return color;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getPermission() {
        return permission;
    }

    public String getLongName() {
        return longName;
    }

    public boolean hasPermission(Player p) {
        return p.hasPermission(this.permission);
    }
}
