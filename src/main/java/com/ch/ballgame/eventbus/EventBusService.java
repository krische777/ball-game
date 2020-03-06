package com.ch.ballgame.eventbus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;
import reactor.fn.Consumer;

import static reactor.bus.selector.Selectors.$;

@Service
public class EventBusService {

    private final EventBus eventBus;

    @Autowired
    public EventBusService(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void fireNotification(final String topic, final Object data) {
       this.eventBus.notify(topic, Event.wrap(data));
    }

    public <T> void subscribe(final String topic, final Consumer<Event<T>> consumer) {
        this.eventBus.on($(topic), consumer);
    }

    public void unregisterConsumer(final String topic) {
        this.eventBus.getConsumerRegistry().unregister(topic);
    }

}
