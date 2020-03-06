package com.ch.ballgame.dto;

import com.ch.ballgame.model.Obstacle;
import com.ch.ballgame.model.Player;

import java.io.Serializable;
import java.util.Collection;

public class BoardStateContainer implements Serializable {

    private Collection<Player> players;
    private Collection<Obstacle> obstacles;

    public Collection<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    public Collection<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setObstacles(Collection<Obstacle> obstacles) {
        this.obstacles = obstacles;
    }
}
