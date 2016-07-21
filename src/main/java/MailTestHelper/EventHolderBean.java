package MailTestHelper;

import org.springframework.stereotype.Component;

/**
 * Created by kenji on 2016/07/05.
 */


@Component
public class EventHolderBean {
    private Boolean eventFired = false;

    public Boolean getEventFired() {
        return eventFired;
    }

    public void setEventFired(Boolean eventFired) {
        this.eventFired = eventFired;
    }
}