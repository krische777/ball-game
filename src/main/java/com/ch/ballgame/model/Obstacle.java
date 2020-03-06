package com.ch.ballgame.model;

import java.util.Objects;
import java.util.UUID;

public class Obstacle extends TwoDObject {
    private String obstacleId = UUID.randomUUID().toString();
    public Obstacle(int x, int y) {
        super(x, y, 10);
    }

    public String getObstacleId() {
        return obstacleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Obstacle obstacle = (Obstacle) o;
        return Objects.equals(obstacleId, obstacle.obstacleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), obstacleId);
    }
}
