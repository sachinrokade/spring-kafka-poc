package com.example.kafka.poc.contorller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kafka.poc.model.ProjectEvent;
import com.example.kafka.poc.service.KafkaTopicService;
import com.example.kafka.poc.service.ProducerService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/projects")
@AllArgsConstructor
public class ProjectController {

	private KafkaTopicService topicService;
    private  ProducerService producerService;

	
	@PostMapping
	public String createOrUpdateProject(@RequestBody ProjectEvent payload) {
		String topicName = payload.getType().toLowerCase() + "-topic";

        // Ensure topic exists
        topicService.createTopicIfNotExists(topicName);

        // Produce message
        producerService.sendMessage(topicName, payload);

        return "Event processed for topic: " + topicName;
	}
}