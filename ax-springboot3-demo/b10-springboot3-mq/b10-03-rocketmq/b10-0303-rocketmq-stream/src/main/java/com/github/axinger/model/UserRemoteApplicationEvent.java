package com.github.axinger.model;

import lombok.Getter;
import org.springframework.cloud.bus.event.RemoteApplicationEvent;


@Getter
public class UserRemoteApplicationEvent extends RemoteApplicationEvent {

    private final User user;

    public UserRemoteApplicationEvent(User user, String originService, String destinationService) {
        super();
//        super(user, originService, DEFAULT_DESTINATION_FACTORY.getDestination(originService));
        this.user = user;
    }

}
