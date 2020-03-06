package com.ch.ballgame.model;

import java.util.UUID;

public class Player extends TwoDObject {

    public static final int PLAYER_SIZE = 20;
    private String userName;
    private int score;
    private Direction direction;
    private UUID uuid;

    public Player(int x, int y, String userName, int score,
                  Direction direction) {
        super(x, y, PLAYER_SIZE);
        this.userName = userName;
        this.score = score;
        this.direction = direction;
        this.uuid=UUID.randomUUID();
    }

    public String getUserName() {
        return userName;
    }

    public Direction getDirection() {
        return direction;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
