package com.orz.base.dao;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Service;

@Service("naverRowPriceDao")
public interface NaverRowPriceDao {

	public ArrayList<HashMap<String, Object>> selectReseachReservation() throws Exception;

	public ArrayList<HashMap<String, Object>> selectItem() throws Exception;
}
