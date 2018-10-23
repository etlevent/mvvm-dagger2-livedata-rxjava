package ext.arch.components.event;

import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import ext.arch.components.util.Preconditions;

public class BaseEvent implements Event {

    @NonNull
    private final EventBus eventBus;

    public BaseEvent() {
        eventBus = EventBus.getDefault();
    }

    public BaseEvent(@NonNull EventBus eventBus) {
        this.eventBus = Preconditions.checkNotNull(eventBus);
    }

    @Override
    public void post() {
        eventBus.post(this);
    }
}
