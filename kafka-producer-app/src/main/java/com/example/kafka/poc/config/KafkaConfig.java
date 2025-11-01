//package com.example.kafka.poc.config;
//
//import org.apache.kafka.clients.admin.NewTopic;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class KafkaConfig {
//
//	@Value("${app.kafka.topics.create}")
//	private String create;
//	
//	@Value("${app.kafkatopics.update}")
//	private String update;
//
//	
//	
//	public String getCreate() {
//		return create;
//	}
//
//	public void setCreate(String create) {
//		this.create = create;
//	}
//
//	public String getUpdate() {
//		return update;
//	}
//
//	public void setUpdate(String update) {
//		this.update = update;
//	}
//
//	@Bean
//	public NewTopic createTopic() {
//		System.out.println("---createTopic created---- ");
//		return new NewTopic(create, 1, (short) 1);
//	}
//
//	@Bean
//	public NewTopic updateTopic() {
//		System.out.println("---updateTopic created---- ");
//		return new NewTopic(update, 1, (short) 1);
//	}
//}