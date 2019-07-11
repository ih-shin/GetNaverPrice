package com.orz.base.service.impl;

import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orz.base.service.NaverSearchAsyncService;
import com.orz.base.service.RabbitMqService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("rabbitMqService")
public class RabbitMqServiceImpl implements RabbitMqService{

	public static final String QUEUE_NAME  = "search.queue";
	
	@Resource(name="naverSearchAsyncService")
	private NaverSearchAsyncService naverSearchAsyncService;
	
	@Override
	@RabbitListener(queues = QUEUE_NAME)
	public void receiveSearchQueue(String json) throws Exception {
		HashMap<String,Object> item = new ObjectMapper().readValue(json, HashMap.class);
		naverSearchAsyncService.NaverSearchNonApi(item);
	}
}
