package com.ch.ballgame.engine;

import com.ch.ballgame.dto.BoardStateContainer;
import com.ch.ballgame.eventbus.EventBusService;
import com.ch.ballgame.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameNotificationsManager {

    private EventBusService eventBusService;

    @Autowired
    public GameNotificationsManager(EventBusService eventBusService) {
        this.eventBusService = eventBusService;
    }

    //send ws messages to all players in the room
    public void updateBoard(Room room, BoardStateContainer boardStateContainer) {
        this.eventBusService.fireNotification(room.getRoomId(), boardStateContainer);
    }

    public void closeRoom(Room room) {
        this.eventBusService.unregisterConsumer(room.getRoomId());
    }
}
