package com.example.kafka.poc.service;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.example.kafka.poc.model.ProjectEvent;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProducerService {

	private final KafkaTemplate<String, ProjectEvent> kafkaTemplate;

	public void sendMessage(String topic, ProjectEvent message) {
		kafkaTemplate.send(topic, message);
		System.out.println("📤 Sent message to " + topic + ": " + message);
	}
}
