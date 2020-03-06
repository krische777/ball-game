package com.ch.ballgame.eventbus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.Environment;
import reactor.bus.EventBus;
import reactor.core.dispatch.WorkQueueDispatcher;
import reactor.fn.Consumer;

@Configuration
public class EventBusConfig {

    @Bean(name = "environment")
    Environment env() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean(name = "eventBus")
    EventBus createEventBus(final Environment env) {

        return EventBus.create(env,
                new WorkQueueDispatcher
                        ("BallGameWorkQueueDispatcher", Runtime.getRuntime().availableProcessors(), 8192, new ExceptionHandler()));
    }

    private class ExceptionHandler implements Consumer<Throwable> {

        @Override
        public void accept(final Throwable throwable) {

            throw new RuntimeException(throwable.getMessage());
        }
    }
}

