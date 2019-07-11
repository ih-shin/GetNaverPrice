package com.orz.base.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class NaverSearchOption implements Serializable {

	private static final long serialVersionUID = -2316570866081739396L;
	
	private String item; 
	private String delevery;
	private String useYn;
	private String sort; // rel(랭킹순) price_asc(최저가순) review(리뷰많은순)
	private int minPrice;
	private int maxPrice;
}
