package com.orz.base.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import lombok.Data;

@Data
public class NaverSearchShoppingResultItems  implements Serializable {

	private static final long serialVersionUID = -4089627625596032246L;

	private String category;
	private String title;
	private String link;
	private String image;
	private double lprice;
	private String mallName;
	private String mallSpace;
	private ArrayList<String> mallOption;
	private ArrayList<HashMap<String, Object>> mallList;
	private String event;
}
