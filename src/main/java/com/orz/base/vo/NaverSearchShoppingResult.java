package com.orz.base.vo;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
public class NaverSearchShoppingResult implements Serializable {

	private static final long serialVersionUID = 5535407052362457385L;

	private String title;
	private String link;
	private String description;
	private String lastBuildDate;
	private int    total;
	private String start;
	private String display;
	private List<NaverSearchShoppingResultItem> items;
}
