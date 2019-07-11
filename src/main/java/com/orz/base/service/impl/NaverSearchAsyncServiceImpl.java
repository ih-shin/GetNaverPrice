package com.orz.base.service.impl;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.orz.base.service.NaverSearchAsyncService;
import com.orz.base.vo.NaverSearchOption;
import com.orz.base.vo.NaverSearchShoppingResult;
import com.orz.base.vo.NaverSearchShoppingResultItems;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("naverSearchAsyncService")
public class NaverSearchAsyncServiceImpl implements NaverSearchAsyncService{

 	@Autowired
	@Qualifier("app")
	private PropertiesFactoryBean app;
 	
	@Override
    @Async("threadPoolTaskExecutor")
	public void NaverSearchApi(HashMap<String, Object> itemMap) throws Exception {

		String apiUrl = "https://openapi.naver.com/v1/search/shop.json?";
		String item   = URLEncoder.encode(String.valueOf(itemMap.get("item")), "UTF-8"); 
				
		double lprice = Integer.valueOf(String.valueOf(itemMap.get("lprice")));

		HttpClient client = HttpClientBuilder.create().build(); 
		HttpGet getRequest = new HttpGet(apiUrl+"query="+item+"&display=100&start=1&sort=dsc"); 
		getRequest.setHeader("Cache-Control", "no-cache"); 
		getRequest.setHeader("Pragma", "no-cache"); 
		getRequest.addHeader("X-Naver-Client-Id"    , String.valueOf(app.getObject().get("naver.client.id")));
		getRequest.addHeader("X-Naver-Client-Secret", String.valueOf(app.getObject().get("naver.client.secret"))); 

		HttpResponse response = client.execute(getRequest);
		
		if (response.getStatusLine().getStatusCode() == 200) {
			ResponseHandler<String> handler = new BasicResponseHandler();
			String body = handler.handleResponse(response);
			Boolean stop = false;
			ArrayList<NaverSearchShoppingResultItems> mailList = new ArrayList<NaverSearchShoppingResultItems>();		
			
			Gson gson = new Gson();
			NaverSearchShoppingResult result = gson.fromJson(body, NaverSearchShoppingResult.class);

			double totCount = result.getTotal();
			double pageCnt  = 100;
			int totPage  = (int) Math.ceil(totCount/pageCnt);
			int page = 1;

			int max = 0;	
			for(int i=1; i <= totPage; i++) {
				
				client = HttpClientBuilder.create().build(); 
				getRequest = new HttpGet(apiUrl+"query="+item+"&display=100&start=" + i + "&sort=dsc"); 
				getRequest.setHeader("Cache-Control", "no-cache"); 
				getRequest.setHeader("Pragma", "no-cache"); 
				getRequest.addHeader("X-Naver-Client-Id"    , String.valueOf(app.getObject().get("naver.client.id")));
				getRequest.addHeader("X-Naver-Client-Secret", String.valueOf(app.getObject().get("naver.client.secret"))); 
				
				response = client.execute(getRequest);

				//log.info("페이지 : " + i);
				//log.info("apiUrl : " + apiUrl+"query="+item+"&display=100&start=" + i + "&sort=dsc");
				
				if (response.getStatusLine().getStatusCode() == 200) {
					handler = new BasicResponseHandler();
					body = handler.handleResponse(response);
					
					result = gson.fromJson(body, NaverSearchShoppingResult.class);
					
					for(NaverSearchShoppingResultItems items : result.getItems()) {
						if (max < items.getLprice() ) {

							max = (int) items.getLprice(); 

						}
						//log.info(items.getProductId());
						/*
						if(items.getLprice() > lprice) {
							log.info("스탑ㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂㅂ");
							log.info(items.toString());
							stop = true;
							break;
						}
						*/
						if(items.getLprice() <= lprice && items.getTitle().toString().indexOf("썬피싱") > -1 
								                       && items.getTitle().toString().indexOf("지그헤드") > -1 
								                       && items.getTitle().toString().indexOf("벌크") > -1 ) {
							mailList.add(items);
						}
					}
				}
				
				if(stop) break;
			}

			log.info("max : " + String.valueOf(max));
			
		} 
	}
	
	@Override
    @Async("threadPoolTaskExecutor")
	public void NaverSearchNonApi(HashMap<String, Object> itemMap) throws Exception {
		//log.info("map : " + itemMap);
		NaverSearchOption naverSearchOption = new NaverSearchOption();
		naverSearchOption.setDelevery(String.valueOf(itemMap.get("delivery")));
		naverSearchOption.setMaxPrice(Integer.valueOf(String.valueOf(itemMap.get("maxPrice"))));
		naverSearchOption.setMinPrice(Integer.valueOf(String.valueOf(itemMap.get("minPrice"))));
		naverSearchOption.setSort(String.valueOf(itemMap.get("sort")));
		
		String searchUrl = "https://search.shopping.naver.com/search/all.nhn?";
		String item      = String.valueOf(itemMap.get("item")).replace(" ", "%20") ;
		int pageIndex    = 1;
		int pagingSize   = 40; // 기본40개 또는 80개
		String viewList  = "list";
		String sort      = naverSearchOption.getSort(); // rel(랭킹순) price_asc(최저가순) review(리뷰많은순)
		String frm       = "NVSHPRC";

		log.info("검색 : " + item + " 최소가격 : " + naverSearchOption.getMinPrice() + " 최대가격 : " + naverSearchOption.getMaxPrice());
		
		searchUrl += "origQuery="+ item;
		searchUrl += "&";
		if("1".equals(String.valueOf(naverSearchOption.getDelevery()))) {
			log.info("검색 : 무료배송");
			searchUrl += "delivery="+naverSearchOption.getDelevery();
		}
		searchUrl += "&";
		searchUrl += "pagingIndex="+pageIndex;
		searchUrl += "&";
		searchUrl += "pagingSize="+pagingSize;
		searchUrl += "&";
		searchUrl += "sort="+sort;
		searchUrl += "&";
		searchUrl += "minPrice="+naverSearchOption.getMinPrice();
		searchUrl += "&";
		searchUrl += "maxPrice="+naverSearchOption.getMaxPrice();
		searchUrl += "&";
		searchUrl += "frm="+frm;
		searchUrl += "&";
		searchUrl += "query="+item;
		
		Document doc = Jsoup.connect(searchUrl).get();

		double totSize = Double.parseDouble(doc.getElementsByClass("_productSet_total").get(0).text().replaceAll("[^0-9]", "").trim());
		int totPage = (int) Math.ceil(totSize/pagingSize);

		log.info("전체 : " + totSize);
		log.info("총 페이지 : " + totPage);
		
		ArrayList<NaverSearchShoppingResultItems> mailList = new ArrayList<NaverSearchShoppingResultItems>();
		
		for(int i=1; i<=totPage; i++) {
			if(i > 1) searchUrl = searchUrl.replace("pagingIndex="+(i-1), "pagingIndex="+i);
			
			log.info("URL : " + searchUrl);
			//log.info("목록수 : " + doc.getElementsByClass("_itemSection").size());
			
			doc = Jsoup.connect(searchUrl).get();
			
			for(Element element : doc.getElementsByClass("_itemSection")) {
				NaverSearchShoppingResultItems naverSearchShoppingResultItems = new NaverSearchShoppingResultItems();
				naverSearchShoppingResultItems.setTitle(element.getElementsByClass("_productLazyImg").get(0).attr("alt"));
				naverSearchShoppingResultItems.setImage(element.getElementsByClass("_productLazyImg").get(0).attr("src"));
				naverSearchShoppingResultItems.setLink(element.getElementsByClass("img_area").get(0).child(0).attr("href"));
				if(element.getElementsByClass("_price_reload").text().replace(",", "").trim().length() > 0) {
					naverSearchShoppingResultItems.setLprice(Double.parseDouble(element.getElementsByClass("_price_reload").text().replaceAll("[^0-9]", "")));
				}
				else {
					naverSearchShoppingResultItems.setLprice(Double.parseDouble(element.getElementsByClass("price_won").text().replaceAll("[^0-9]", "")));
				}
				naverSearchShoppingResultItems.setEvent(element.getElementsByClass("event").text());
				
				if(element.getElementsByClass("mall_img").get(0).getElementsByTag("img").attr("alt").length() == 0) {
					naverSearchShoppingResultItems.setMallName(element.getElementsByClass("mall_txt").get(0).child(0).text());
				}
				else {
					naverSearchShoppingResultItems.setMallName(element.getElementsByClass("mall_img").get(0).getElementsByTag("img").attr("alt"));
				}
				
				if(element.getElementsByClass("mall_list").size() > 0) {
					Iterator<Element> list = element.getElementsByClass("mall_list").select("li").iterator();
					ArrayList<HashMap<String, Object>> mallList = new ArrayList<HashMap<String, Object>>();
					while(list.hasNext()) {
						Element elementMall = list.next();

						for(String s : elementMall.child(0).toString().split(" ")) {
							HashMap<String, Object> m = new HashMap<String, Object>();
							
							if(s.split("=").length > 1) {
								m.put(s.split("=")[0], s.split("=")[1]);
								mallList.add(m);
							}
						}
					}
					naverSearchShoppingResultItems.setMallList(mallList);
				}
				
				if(element.getElementsByClass("mall_option").size() > 0) {
					Iterator<Element> list = element.getElementsByClass("mall_option").select("li").iterator();
					ArrayList<String> optionList = new ArrayList<String>();
					while(list.hasNext()) {
						Element elementMall = list.next();
						String option = elementMall.text().trim().replace("구매정보", "");
						
						if(option.length() > 0)
							optionList.add(option);
					}
					naverSearchShoppingResultItems.setMallOption(optionList);
				}

				naverSearchShoppingResultItems.setCategory(element.getElementsByClass("depth").text().split(">")[0].trim());

				if(mailList.size() > 9) break;
				
				mailList.add(naverSearchShoppingResultItems);
				
			}

			if(mailList.size() > 9) break;
		}
		
		for(NaverSearchShoppingResultItems a : mailList) { 
			log.info("판매처 : " + a.getMallName() + " 가격 : " + a.getLprice() + " 링크 : " + a.getLink() );
		}
		
	}
}
