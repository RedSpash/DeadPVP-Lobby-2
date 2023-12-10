package net.deadpvp.lobby.rank;

import org.bukkit.entity.Player;

public class Rank {

    private final int power;
    private final String permission;
    private final String name;
    private final String color;

    public Rank(int power, String permission, String name, String color) {
        this.power = power;
        this.permission = permission;
        this.name = name;
        this.color = color;
    }

    public int getPower() {
        return power;
    }

    public String getColor() {
        return color;
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public boolean hasPermission(Player p) {
        return p.hasPermission(this.permission);
    }
}
