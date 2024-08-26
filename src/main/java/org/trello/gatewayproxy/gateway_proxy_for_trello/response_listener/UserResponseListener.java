package org.trello.gatewayproxy.gateway_proxy_for_trello.response_listener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Azizbek Toshpulatov
 */
@Service
public class UserResponseListener {
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private final Map<String, CompletableFuture<Object>> responseMap = new ConcurrentHashMap<>();

    @KafkaListener(topics = "user-responses", groupId = "response-group")
    public void listenUserResponses(String key, Object result) {
        CompletableFuture<Object> future = responseMap.remove(key);
        if (future != null) {
            future.complete(result);
        }
    }

    public Object sendRequestAndGetResponse(String key, Object request) throws Exception {
        CompletableFuture<Object> future = new CompletableFuture<>();
        responseMap.put(key, future);
        kafkaTemplate.send("user-requests", key, request);
        return future.get();
    }
}
