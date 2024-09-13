package org.trello.gatewayproxy.gateway_proxy_for_trello.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.trello.gatewayproxy.gateway_proxy_for_trello.kafka.messages.KafkaEvent;
import org.trello.gatewayproxy.gateway_proxy_for_trello.model.User;

import java.util.List;

/**
 * @author Azizbek Toshpulatov
 */
@Service
public class KafkaConsumer {
    private KafkaEvent<User> userEvent;
    private KafkaEvent<List<User>> usersEvent;
    private KafkaEvent<String> messageEvent;


    // tickets and jobs

    @KafkaListener(topics = "User", groupId = "trelloProxy")
    public void consumeUser(KafkaEvent<User> event) {
        userEvent = event;
    }

    @KafkaListener(topics = "Users", groupId = "trelloProxy")
    public void consumeAllUsers(KafkaEvent<List<User>> users) {
        usersEvent = users;
    }

    @KafkaListener(topics = "String", groupId = "trelloProxy")
    public void consumeMessage(KafkaEvent<String> message) {
        messageEvent = message;
    }

    public List<User> getUsers() {
        return usersEvent.getData();
    }

    public User getUser() {
        return userEvent.getData();
    }

    public String getMessage() {
        return messageEvent.getData();
    }

    public KafkaEvent<User> getUserEvent() {
        return userEvent;
    }

    public KafkaEvent<List<User>> getUsersEvent() {
        return usersEvent;
    }

    public KafkaEvent<String> getMessageEvent() {
        return messageEvent;
    }
}
