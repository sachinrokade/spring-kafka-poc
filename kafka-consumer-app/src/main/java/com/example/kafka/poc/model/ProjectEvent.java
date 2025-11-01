package com.example.kafka.poc.model;

import java.time.Instant;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ProjectEvent {
	private String projectId;
	private String name;
	private String description;
	private String type; // CREATE or UPDATE
	private Instant timestamp;

}