package com.ch.ballgame.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import static com.ch.ballgame.engine.GameEngine.MAX_NUM_PLAYERS;

public class Room {
    private String roomId = UUID.randomUUID().toString();
    private String roomName;
    private boolean isFull = false;
    private List<Player> playerList = new ArrayList<>();
    private static AtomicInteger roomCounter = new AtomicInteger(0);
    private static Room currentActiveRoom = new Room("room:" + roomCounter.incrementAndGet());

    public Room(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public boolean addPlayer(Player player) {
        if (playerList.size() <= MAX_NUM_PLAYERS) {
            return playerList.add(player);
        } else {
            this.isFull = true;
            return false;
        }
    }

    public synchronized static Room getCurrentFreeRoom() {
        return currentActiveRoom;
    }

    public synchronized static void closeCurrentFreeRoom() {
        currentActiveRoom = new Room("room:" + roomCounter.incrementAndGet());
    }

    public boolean isFull() {
        return isFull;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }
}
