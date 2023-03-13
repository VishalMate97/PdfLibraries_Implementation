package com.utility;

public class cellInfo { 

	private float x;
	private float y;
	private float cellEndX;
	private float cellEndY;
	private float width;
	private float height;
	private String cellNo;
	private float lowestHeight;
	
	public float getLowestHeight() {
		return lowestHeight;
	}

	public void setLowestHeight(float lowestHeight) {
		this.lowestHeight = lowestHeight;
	}

	public cellInfo(float x, float y, float width,  float height, String cellNo, float cellEndX, float cellEndY) {
		super();
		this.x = x;
		this.y = y;
		this.height = height;
		this.width = width;
		this.cellNo = cellNo;
		this.cellEndX = cellEndX;
		this.cellEndY = cellEndY;
	}

	public float getCellEndX() {
		return cellEndX;
	}

	public void setCellEndX(float cellEndX) {
		this.cellEndX = cellEndX;
	}

	public float getCellEndY() {
		return cellEndY;
	}

	public void setCellEndY(float cellEndY) {
		this.cellEndY = cellEndY;
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
