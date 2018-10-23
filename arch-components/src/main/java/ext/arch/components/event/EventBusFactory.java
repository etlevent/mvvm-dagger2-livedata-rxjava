package ext.arch.components.event;


import org.greenrobot.eventbus.EventBus;

import ext.arch.components.BuildConfig;

public final class EventBusFactory {
    private static EventBus _eventBus;

    static {
        _eventBus = EventBus.builder()
                .sendNoSubscriberEvent(false)
                .sendSubscriberExceptionEvent(false)
                .throwSubscriberException(BuildConfig.DEBUG)
                .build();
    }

    private EventBusFactory() {
        throw new AssertionError("no instance.");
    }

    public static EventBus throwableEventBusIfNeeded() {
        return _eventBus;
    }
}
