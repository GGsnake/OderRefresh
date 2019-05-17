package com.neo.task;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObjectSender {

	@Autowired
	private AmqpTemplate rabbitTemplate;

	public void send(String json) {
		System.out.println("Sender object: " + json);
		this.rabbitTemplate.convertAndSend("topicExchange","jh.order", json);
	}

}