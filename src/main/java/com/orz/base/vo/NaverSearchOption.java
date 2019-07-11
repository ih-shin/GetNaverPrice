package com.orz.base.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class NaverSearchOption implements Serializable {

	private static final long serialVersionUID = -2316570866081739396L;
	
	private String item; //상품명    
	private String delevery; // 무료배송여부 1(무료배송)
	private String useYn; // 상품 사용여부
	private String sort; // rel(랭킹순) price_asc(최저가순) review(리뷰많은순)
	private int minPrice; // 최소가
	private int maxPrice; // 최대가
}
