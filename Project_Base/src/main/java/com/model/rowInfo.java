package com.model;

//rowInfo Will work for Horizontal row - Key Value Pair
public class rowInfo {
	private String key;
	private String value;
	private int seq;
	public rowInfo(String key, String value, int seq) {
		super();
		this.key = key;
		this.value = value;
		this.seq = seq;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	
	
}
