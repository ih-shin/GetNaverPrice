package com.orz.base.service;

import java.util.HashMap;

public interface NaverSearchAsyncService {
	public void NaverSearchApi(HashMap<String, Object> item) throws Exception;
	
	public void NaverSearchNonApi(HashMap<String, Object> item) throws Exception;
}
