package com.utility;

import java.util.List;

public class seriesList {

	private List<cellInfo> cellList;
	private float lowestHeight;
	
	
	
	public seriesList(List<cellInfo> cellList, float lowestHeight) {
		super();
		this.cellList = cellList;
		this.lowestHeight = lowestHeight;
	}
	public List<cellInfo> getCellList() {
		return cellList;
	}
	public void setCellList(List<cellInfo> cellList) {
		this.cellList = cellList;
	}
	public float getLowestHeight() {
		return lowestHeight;
	}
	public void setLowestHeight(float lowestHeight) {
		this.lowestHeight = lowestHeight;
	}

	
}
