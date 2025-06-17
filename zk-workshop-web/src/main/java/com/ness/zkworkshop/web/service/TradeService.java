package com.ness.zkworkshop.web.service;

import java.util.*;

import com.ness.zkworkshop.web.model.Trade;

public class TradeService {

	private TreeMap<Integer, Trade> tradeTable = new TreeMap<>();
	private int sequence = 0;
	
	public TradeService(){
		save(new Trade("Nápoje", "Suyama", 5122));
		save(new Trade("Maso", "Davolio", 450));
		save(new Trade("Výroba", "Buchanan", 6328));
		save(new Trade("Nástroje", "Suyama", 230));
		save(new Trade("Zelenina", "John", 947));
	}

	public void save(Map<Integer, Trade> changedMap){
		for(Map.Entry<Integer, Trade> entry: changedMap.entrySet()) {
			Trade changedTrade = entry.getValue();
			tradeTable.put(changedTrade.getId(), changedTrade); //simplified logic, update without comparing
		}
	}
	
	public void save(Trade trade){
		if (trade.getId()==null){
			trade.setId(nextId());
		}
		tradeTable.put(trade.getId(), trade);
	}
	
	public Map<Integer, Trade> queryAll(){
		TreeMap<Integer, Trade> result = new TreeMap<>();
		for (Map.Entry<Integer, Trade> entry : tradeTable.entrySet()){
			result.put(entry.getKey(), new Trade(entry.getValue()));
		}
		return result;
	}
	
	private Integer nextId(){
		return sequence++;
	}
}
