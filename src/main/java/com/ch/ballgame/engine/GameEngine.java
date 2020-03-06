package com.ch.ballgame.engine;

import com.ch.ballgame.dto.BoardStateContainer;
import com.ch.ballgame.model.Direction;
import com.ch.ballgame.model.Obstacle;
import com.ch.ballgame.model.Player;
import com.ch.ballgame.model.Room;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameEngine implements Runnable {
    public static final int UP_ARROW = 38;
    public static final int DOWN_ARROW = 40;
    public static final int LEFT_ARROW = 37;
    public static final int RIGHT_ARROW = 39;
    private final Logger LOGGER = LoggerFactory.getLogger(GameEngine.class);

    public static final int BALL_REFRESH_INTERVAL = 20;
    public static final int NUMBER_OF_OBSTACLES = 31;
    public static final int CANVAS_WIDTH = 1200;
    public static final int CANVAS_HEIGHT = 700;
    public static final Integer MAX_NUM_PLAYERS = 5;
    public static final int BALL_SPEED = 10;

    public Map<String, Obstacle> obstacles = new HashMap<>();
    public Map<String, Player> players = new ConcurrentHashMap<>();
    public static Map<String, GameEngine> ACTIVE_GAMES = new ConcurrentHashMap<>();

    private Room room;
    private String gameId;
    private boolean isRunning = true;
    private GameNotificationsManager gameNotificationsManager;
    private static String CURRENT_GAME_ID = UUID.randomUUID().toString();

    public GameEngine(Room room, GameNotificationsManager gameNotificationsManager, String gameId) {
        this.room = room;
        addAllPlayersFromRoom(room);
        this.gameNotificationsManager = gameNotificationsManager;
        this.gameId = gameId;
    }

    public synchronized static String getCurrentGameId() {
        return CURRENT_GAME_ID;
    }

    public synchronized static void refreshGameId() {
        CURRENT_GAME_ID = UUID.randomUUID().toString();
    }

    private void addAllPlayersFromRoom(Room room) {
        room.getPlayerList().forEach(this::addPlayer);
    }

    public static GameEngine findGame(String gameId){
        return ACTIVE_GAMES.get(gameId);
    }

    @Override
    public void run() {
        Room.closeCurrentFreeRoom();
        registerGame();
        generateObstacles();
        LOGGER.info("Starting game ...");
        while (isRunning) {
            updatePlayersAndObstacles(players, obstacles);
            sleep(BALL_REFRESH_INTERVAL);
        }
    }

    private void registerGame() {
        ACTIVE_GAMES.put(this.gameId, this);
    }

    private void generateObstacles() {
        Random random = new Random();
        for (int i = 0; i < NUMBER_OF_OBSTACLES; i++) {
            int x = random.nextInt(CANVAS_WIDTH);
            int y = random.nextInt(CANVAS_HEIGHT);
            this.addObstacle(new Obstacle(x, y));
        }
    }

    private void updatePlayersAndObstacles(Map<String, Player> players, Map<String, Obstacle> obstacles) {
        for (Player player : players.values()) {
            Direction direction = player.getDirection();
            player.setX(player.getX() + direction.getDx());
            player.setY(player.getY() + direction.getDy());
            if (player.getX() > CANVAS_WIDTH || player.getX() < 0) {
                direction.setDx(-direction.getDx());
            }
            if (player.getY() > CANVAS_HEIGHT || player.getY() < 0) {
                direction.setDy(-direction.getDy());
            }
        }
        Set<String> obstaclesToRemove = new HashSet<>();
        for (Player player : players.values()) {
            for (Obstacle obstacle : obstacles.values()) {
                if (Math.pow(obstacle.getX() - player.getX(), 2)
                        + Math.pow(obstacle.getY() - player.getY(), 2) < Math.pow(player.getSize(), 2)) {
                    player.setScore(player.getScore() + 1);
                    obstaclesToRemove.add(obstacle.getObstacleId());
                }
            }
        }

        for (String obstacleId : obstaclesToRemove) {
            this.obstacles.remove(obstacleId);
        }
        BoardStateContainer boardStateContainer = new BoardStateContainer();
        boardStateContainer.setObstacles(obstacles.values());
        boardStateContainer.setPlayers(players.values());
        gameNotificationsManager.updateBoard(room, boardStateContainer);
        if (obstacles.size() == 0) {
            System.out.println("Game won");
            stopGame();
        }

    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void stopGame() {
        this.isRunning = false;
        //clear all consumers for events for this room
        this.gameNotificationsManager.closeRoom(room);
    }

    public void addPlayer(Player player) {
        players.put(player.getUserName(), player);
    }

    public void addObstacle(Obstacle obstacle) {
        obstacles.put(obstacle.getObstacleId(), obstacle);
    }

    public void updatePlayerDirection(String userName, String keyCode) {
        Direction direction = this.players.get(userName).getDirection();
        if (Integer.parseInt(keyCode) == UP_ARROW && direction.getDy() == BALL_SPEED) {
            direction.setDy(-direction.getDy());
        }
        if (Integer.parseInt(keyCode) == LEFT_ARROW && direction.getDx() == BALL_SPEED) {
            direction.setDx(-direction.getDx());
        }
        if (Integer.parseInt(keyCode) == DOWN_ARROW && direction.getDy() == -BALL_SPEED) {
            direction.setDy(-direction.getDy());
        }
        if (Integer.parseInt(keyCode) == RIGHT_ARROW && direction.getDx() == -BALL_SPEED) {
            direction.setDx(-direction.getDx());
        }
    }

    public String getGameId() {
        return gameId;
    }
}
