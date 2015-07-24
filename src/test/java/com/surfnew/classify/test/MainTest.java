package com.surfnew.classify.test;

import java.util.List;
import java.util.Map;

import com.cherry.command.api.client.HBaseAPIService;

public class MainTest {
	
	public static void main(String[] args) {
		List<Map<String, String>> hbaseQueryDatasWithRow = HBaseAPIService.getHbaseQueryDatasWithRow("mobile_score", "13775858786");
		System.out.println(hbaseQueryDatasWithRow);
	}

}
