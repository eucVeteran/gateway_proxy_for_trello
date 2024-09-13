package org.trello.gatewayproxy.gateway_proxy_for_trello.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.trello.gatewayproxy.gateway_proxy_for_trello.kafka.messages.KafkaCommand;
import org.trello.gatewayproxy.gateway_proxy_for_trello.model.User;

import java.util.List;
import java.util.UUID;

/**
 * @author Azizbek Toshpulatov
 */
@Service
public class KafkaProducer {
    private final KafkaTemplate<String, User> kafkaUserTemplate;
    private final KafkaTemplate<String, List<User>> kafkaUserListTemplate;
    private final KafkaTemplate<String, String> kafkaStringTemplate;

    private final KafkaTemplate<String, KafkaCommand<Long>> kafkaLongTemplate;

    public KafkaProducer(KafkaTemplate<String, User> kafkaUserTemplate,
                         KafkaTemplate<String, List<User>> kafkaUserListTemplate,
                         KafkaTemplate<String, String> kafkaStringTemplate,
                         KafkaTemplate<String, KafkaCommand<Long>> kafkaLongTemplate) {
        this.kafkaUserTemplate = kafkaUserTemplate;
        this.kafkaUserListTemplate = kafkaUserListTemplate;
        this.kafkaStringTemplate = kafkaStringTemplate;
        this.kafkaLongTemplate = kafkaLongTemplate;
    }

    public UUID sendUser(KafkaActions action, User user) {
        var id = UUID.randomUUID();

        var message = MessageBuilder
                .withPayload(new KafkaCommand<>(id, user, action))
                .setHeader(KafkaHeaders.TOPIC, "User")
                .build();

        kafkaUserTemplate.send(message);

        return id;
    }

    public UUID requestUserList(KafkaActions action, List<User> users) {
        var id = UUID.randomUUID();

        var message = MessageBuilder
                .withPayload(new KafkaCommand<>(id, users, action))
                .setHeader(KafkaHeaders.TOPIC, "Users")
                .build();

        kafkaUserListTemplate.send(message);

        return id;
    }

    public UUID sendString(KafkaActions action, String data) {
        var id = UUID.randomUUID();

        var message = MessageBuilder
                .withPayload(new KafkaCommand<>(id, data, action))
                .setHeader(KafkaHeaders.TOPIC, "String")
                .build();

        kafkaStringTemplate.send(message);

        return id;
    }

    public UUID sendLong(KafkaActions action, Long data) {
        var id = UUID.randomUUID();

        var message = MessageBuilder
                .withPayload(new KafkaCommand<>(id, data, action))
                .setHeader(KafkaHeaders.TOPIC, "Long")
                .build();

        kafkaLongTemplate.send(message);

        return id;
    }
}
