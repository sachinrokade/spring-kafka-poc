package com.example.kafka.poc.service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class KafkaTopicService {

    private final AdminClient adminClient;

    public void createTopicIfNotExists(String topicName) {
        try {
            ListTopicsResult topics = adminClient.listTopics();
            Set<String> existingTopics = topics.names().get();

            if (!existingTopics.contains(topicName)) {
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
                adminClient.createTopics(Collections.singleton(newTopic));
                System.out.println(" Created topic: " + topicName);
            } else {
                System.out.println(" Topic already exists: " + topicName);
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error checking/creating topic: " + topicName, e);
        }
    }
}
