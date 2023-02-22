package com.utility;

public class cellInfo { 

	private float x;
	private float y;
	private float width;
	private float height;
	private String cellNo;
	
	public cellInfo(float x, float y, float width,  float height, String cellNo) {
		super();
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.cellNo = cellNo;
	}

	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public float getWidth() {
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}

	public String getCellNo() {
		return cellNo;
	}

	public void setCellNo(String cellNo) {
		this.cellNo = cellNo;
	}
	
	
	
}
