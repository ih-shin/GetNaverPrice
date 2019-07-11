package com.orz.base.scheduler;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.orz.base.service.NaverRowPriceService;

@Component
@EnableScheduling
public class NaverRowPriceScheduler {
 	
	@Resource(name = "naverRowPriceService")
	private NaverRowPriceService naverRowPriceService;	

    //@Scheduled(cron = "${scheduler.naver.rowPrice}")
	@Scheduled(fixedDelayString = "1000000000" )
	public void naverRowPrice() throws Exception {
		naverRowPriceService.start();
	}
}
