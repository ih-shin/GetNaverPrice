package com.orz.base.service.impl;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Resource;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.orz.base.dao.NaverRowPriceDao;
import com.orz.base.service.NaverRowPriceService;

@Service("naverRowPriceService")
public class NaverRowPriceServiceImpl implements NaverRowPriceService{

	public static final String QUEUE_NAME  = "search.queue";

	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Resource(name="naverRowPriceDao")
	private NaverRowPriceDao naverRowPriceDao;

	public void insertQueue(HashMap<String, Object> item) throws Exception {
		Gson gson = new Gson();
		String jsonArray = gson.toJson(item);
	    rabbitTemplate.convertAndSend(QUEUE_NAME,jsonArray);
	}

	@Override
	public void start() throws Exception {
		
		ArrayList<HashMap<String, Object>> itemList = new ArrayList<HashMap<String, Object>>();
		itemList = naverRowPriceDao.selectItem();
		
		for(HashMap<String, Object> item : itemList) {
			insertQueue(item);
		}
	}
}
