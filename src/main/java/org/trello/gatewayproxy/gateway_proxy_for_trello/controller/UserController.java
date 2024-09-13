package org.trello.gatewayproxy.gateway_proxy_for_trello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trello.gatewayproxy.gateway_proxy_for_trello.dto.UserDto;
import org.trello.gatewayproxy.gateway_proxy_for_trello.kafka.KafkaProducer;
import org.trello.gatewayproxy.gateway_proxy_for_trello.kafka.KafkaConsumer;
import org.trello.gatewayproxy.gateway_proxy_for_trello.model.User;

import java.util.List;

import static org.trello.gatewayproxy.gateway_proxy_for_trello.kafka.KafkaActions.*;

/**
 * @author Azizbek Toshpulatov
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private KafkaProducer kafkaProducer;

    @Autowired
    private KafkaConsumer kafkaConsumer;

    @PostMapping
    public User createUser(@RequestBody User user) throws InterruptedException {
        var commandId = kafkaProducer.sendUser(CREATE, user);

        while (kafkaConsumer.getUserEvent() == null || kafkaConsumer.getUserEvent().getCommandId() != commandId) {
            Thread.sleep(500);
        }

        return kafkaConsumer.getUser();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws InterruptedException {
        var commandId = kafkaProducer.sendLong(GET, id);

        while (kafkaConsumer.getUserEvent() == null || kafkaConsumer.getUserEvent().getCommandId() != commandId) {
            Thread.sleep(500);
        }

        return kafkaConsumer.getUser();
    }

    @GetMapping
    public List<User> getAllUsers() throws InterruptedException {
        var commandId = kafkaProducer.sendString(GET, null);

        while (kafkaConsumer.getUsersEvent() == null || kafkaConsumer.getUsersEvent().getCommandId() != commandId) {
            Thread.sleep(500);
        }

        return kafkaConsumer.getUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) throws InterruptedException {
        var commandId = kafkaProducer.sendUser(UPDATE, userDetails);

        while (kafkaConsumer.getUserEvent() == null || kafkaConsumer.getUserEvent().getCommandId() != commandId) {
            Thread.sleep(500);
        }

        return kafkaConsumer.getUser();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        kafkaProducer.sendLong(DELETE, id);
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDto userDto) throws InterruptedException {
        var user = new User(0L, userDto.username(), userDto.email(), userDto.password());
        var commandId = kafkaProducer.sendUser(CREATE, user);

        while (kafkaConsumer.getMessageEvent() == null || kafkaConsumer.getMessageEvent().getCommandId() != commandId) {
            Thread.sleep(500);
        }

        return kafkaConsumer.getMessage();
    }
}