package com.ch.ballgame.engine;

import com.ch.ballgame.dto.BoardStateContainer;
import com.google.gson.Gson;
import org.springframework.web.socket.WebSocketSession;
import reactor.bus.Event;
import reactor.fn.Consumer;

import static com.ch.ballgame.util.ServiceUtiil.sendMessage;

public class NotificationDispatcher implements Consumer<Event<BoardStateContainer>> {

    private final Gson gson;
    private final WebSocketSession webSocketSession;

    public NotificationDispatcher(final WebSocketSession webSocketSession, final Gson gson) {
        this.webSocketSession = webSocketSession;
        this.gson = gson;
    }

    @Override
    public void accept(final Event<BoardStateContainer> event) {
        sendMessage(this.webSocketSession, this.gson.toJson(event.getData()));
    }

}
