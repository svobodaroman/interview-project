package com.wh.interview.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
public class NotificationDispatcher {

    private final SimpMessagingTemplate template;
    private final Set<String> subscribers = new HashSet<>();

    @Autowired
    public NotificationDispatcher(SimpMessagingTemplate template) {
        this.template = template;
    }

    public void dispatchMessage(@Nonnull String message) {
        for (String subscriberId : subscribers) {
            log.info("Sending to subscriberId {} message: {}", subscriberId, message);

            SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
            headerAccessor.setSessionId(subscriberId);
            headerAccessor.setLeaveMutable(true);

            template.convertAndSendToUser(
                    subscriberId,
                    "/notification/score",
                    new NotificationMessage(message),
                    headerAccessor.getMessageHeaders());
        }
    }

    public void addSubscriber(String sessionId) {
        subscribers.add(sessionId);
    }

    public void removeSubscriber(String sessionId) {
        subscribers.remove(sessionId);
    }

    @EventListener
    public void sessionDisconnectionHandler(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.info("Disconnecting " + sessionId + "!");
        removeSubscriber(sessionId);
    }
}
