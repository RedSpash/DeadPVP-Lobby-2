package net.deadpvp.lobby.menu.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class TeleportCommand implements Command{

    private final double x;
    private final double y;
    private final double z;
    private final String world;
    private final float yaw;
    private final float pitch;

    public TeleportCommand(double x, double y, double z, float pitch, float yaw, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
        this.world = world;
    }


    @Override
    public void execute(Player p) {
        Location location = new Location(p.getWorld(),this.x,this.y,this.z,this.yaw,this.pitch);

        if(this.world != null){
            location.setWorld(Bukkit.getWorld(this.world));
        }

        p.teleport(location);
    }
}
