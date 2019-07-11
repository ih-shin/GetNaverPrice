package com.orz.base.service;

public interface RabbitMqService {
	public void receiveSearchQueue(String json) throws Exception;
}
