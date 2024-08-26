package org.trello.gatewayproxy.gateway_proxy_for_trello.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.trello.gatewayproxy.gateway_proxy_for_trello.dto.UserDto;
import org.trello.gatewayproxy.gateway_proxy_for_trello.model.User;
import org.trello.gatewayproxy.gateway_proxy_for_trello.response_listener.UserResponseListener;

import java.util.List;

/**
 * @author Azizbek Toshpulatov
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserResponseListener userResponseListener;

    @PostMapping
    public User createUser(@RequestBody User user) throws Exception {
        return (User) userResponseListener.sendRequestAndGetResponse("createUser", user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) throws Exception {
        return (User) userResponseListener.sendRequestAndGetResponse("getUser", String.valueOf(id));
    }

    @GetMapping
    public List<User> getAllUsers() throws Exception {
        return (List<User>) userResponseListener.sendRequestAndGetResponse("getAllUsers", null);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetails) throws Exception {
        return (User) userResponseListener.sendRequestAndGetResponse("updateUser", new UserUpdateRequest(id, userDetails));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) throws Exception {
        userResponseListener.sendRequestAndGetResponse("deleteUser", id);
    }

    @PostMapping("/register")
    public String registerUser(@RequestBody UserDto userDto) throws Exception {
        return (String) userResponseListener.sendRequestAndGetResponse("registerUser", userDto);
    }

    private record UserUpdateRequest(Long id, User userDetails){}
}