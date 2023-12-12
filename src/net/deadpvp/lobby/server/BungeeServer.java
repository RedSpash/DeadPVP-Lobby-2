package net.deadpvp.lobby.server;

public class BungeeServer {
    private int players;
    private int status;

    public BungeeServer(int players, int status) {
        this.players = players;
        this.status = status;
    }

    public void setPlayers(int players) {
        this.players = players;
        if(this.players > 0){
            this.status = 1;
        }
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPlayers() {
        return players;
    }

    public int getStatus() {
        return status;
    }
}
