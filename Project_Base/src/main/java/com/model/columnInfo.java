package com.model;

//rowInfo Will work for columnInfo to work with table's Column
public class columnInfo {
	private int seq;
	private int width;
	private String colName = "";
	public columnInfo( int seq, int width,String colName) {
		super();
		this.width = width;
		this.seq = seq;
		this.colName = colName;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		this.colName = colName;
	}
	
	
}
