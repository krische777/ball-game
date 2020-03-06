package com.ch.ballgame.engine;

import com.ch.ballgame.dto.GameRequest;
import com.ch.ballgame.dto.GameResponse;
import com.ch.ballgame.eventbus.EventBusService;
import com.ch.ballgame.model.Direction;
import com.ch.ballgame.model.Player;
import com.ch.ballgame.model.Room;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Random;

import static com.ch.ballgame.util.ServiceUtiil.sendMessage;
import static com.ch.ballgame.engine.GameEngine.*;

public class GameRequestDispatcher extends TextWebSocketHandler {

    public static final String USER_SESSION_ATTRIBUTE = "user";
    private static final String GAME_ID_SESSION_ATTRIBUTE = "game-id";
    private final Logger logger = LoggerFactory.getLogger(GameRequestDispatcher.class);

    private Gson gson;
    private Random random;
    private EventBusService eventService;
    private GameNotificationsManager gameNotificationsManager;
    private TaskExecutor taskExecutor = new ConcurrentTaskExecutor();

    public GameRequestDispatcher(Gson gson, EventBusService eventService, GameNotificationsManager gameNotificationsManager) {
        this.gson = gson;
        this.eventService = eventService;
        this.gameNotificationsManager = gameNotificationsManager;
        this.random = new Random();
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) {
        logger.info("JsonRequestDispatcher.afterConnectionEstablished");
    }

    @Override
    public void afterConnectionClosed(final WebSocketSession session, final CloseStatus status) {
        logger.info("JsonRequestDispatcher.afterConnectionClosed");
    }

    @Override
    public void handleTextMessage(final WebSocketSession session, final TextMessage message) throws Exception {
        logger.info("JsonRequestDispatcher.handleTextMessage");
        GameRequest request = null;
        String userName;
        try {
            request = this.gson.fromJson(message.getPayload(), GameRequest.class);
            logger.info("Request Received: " + request.toString());
            final String methodToHandle = request.getMethod();
            if (methodToHandle.equals("startGame")) {
                Room currentActiveRoom = Room.getCurrentFreeRoom();
                GameEngine gameEngine = new GameEngine(currentActiveRoom, gameNotificationsManager, getCurrentGameId());
                GameEngine.refreshGameId();
                taskExecutor.execute(gameEngine);

            } else if (methodToHandle.equals("register")) {
                final NotificationDispatcher notificationDispatcher = new NotificationDispatcher(session, this.gson);
                userName = request.getParams().get("name").getAsString();
                Player player = createPlayer(userName);
                Room currentActiveRoom = Room.getCurrentFreeRoom();
                currentActiveRoom.addPlayer(player);
                eventService.subscribe(currentActiveRoom.getRoomId(), notificationDispatcher);
                setUserAttribute(session, userName, USER_SESSION_ATTRIBUTE);
                setUserAttribute(session, GameEngine.getCurrentGameId(), GAME_ID_SESSION_ATTRIBUTE);
                logger.info("Register with name " + userName + " for room " + currentActiveRoom.getRoomId() + " successful");
                GameResponse gameResponse = new GameResponse();
                gameResponse.setResponseMethod(methodToHandle);
                gameResponse.setResult("{\"player\":\"" + player.getUuid() + "\"}");
                sendMessage(session, this.gson.toJson(gameResponse));
            } else {
                Object userAttr = session.getAttributes().get(USER_SESSION_ATTRIBUTE);
                Object gameIdAttr = session.getAttributes().get(GAME_ID_SESSION_ATTRIBUTE);

                if (methodToHandle.equals("keyPress") && userAttr != null && gameIdAttr != null) {
                    String keyCode = request.getParams().get("key").getAsString();
                    logger.info("Key pressed " + keyCode);
                    userName = (String) userAttr;
                    String gameId = (String) gameIdAttr;
                    logger.info("User name " + userName);
                    GameEngine game = findGame(gameId);
                    if (game != null) {
                        game.updatePlayerDirection(userName, keyCode);
                    }
                }
            }
            //handle request
            final String response = "{\"" + request.getMethod() + "\": \"successfull\"}";
            sendMessage(session, response);
        } catch (final Exception e) {
            this.logger.error("", e);
            final GameResponse gameResponse = new GameResponse();
            if (request == null) {
                gameResponse.setError("Unable to parse request.");
            } else {
                gameResponse.setError(e.getMessage());
                gameResponse.setResponseMethod(request.getMethod());
            }
            sendMessage(session, this.gson.toJson(gameResponse));
        }
    }

    private Player createPlayer(String userName) {
        int x = random.nextInt(CANVAS_WIDTH);
        int y = random.nextInt(CANVAS_HEIGHT);
        int dx = BALL_SPEED * (random.nextBoolean() ? 1 : -1);
        int dy = BALL_SPEED * (random.nextBoolean() ? 1 : -1);
        return new Player(x, y, userName, 0, new Direction(dx, dy));
    }

    private void setUserAttribute(WebSocketSession session, String userName, String userSessionAttribute) {
        Map<String, Object> attributes = session.getAttributes();
        attributes.put(userSessionAttribute, userName);
    }

}
