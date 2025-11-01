
package com.example.servicea.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.kafka.poc.model.ProjectEvent;

@Component
public class ProjectListenerA {

    @KafkaListener(topics = "projects.create", groupId = "service-a-group")
    public void onCreate(ProjectEvent event) {
        System.out.println("[Service A] CREATE event received: " + event.getProjectId() + " name=" + event.getName());
    }

    @KafkaListener(topics = "projects.update", groupId = "service-a-group")
    public void onUpdate(ProjectEvent event) {
        System.out.println("[Service A] UPDATE event received: " + event.getProjectId() + " name=" + event.getName());
    }
}
