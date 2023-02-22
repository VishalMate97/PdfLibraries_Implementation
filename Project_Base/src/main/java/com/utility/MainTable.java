package com.utility;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class MainTable {
	private float initX;
	private float initY;
	private float tblWidth;
	private float tblHeight;
	private float noOfCols;
	private float noOfRows;
	private PDPageContentStream contentStream;

	private float singleColSpan;
	private float singleRowSpan;

//	private List<Map<String,Integer>> allCell = new ArrayList<Map<String,Integer>>();

	public MainTable(float initX, float initY, float tblWidth, float tblHeight, float noOfCols, float noOfRows,
			PDPageContentStream contentStream) {
		super();
		this.initX = initX;
		this.initY = initY;
		this.tblHeight = tblHeight;
		this.tblWidth = tblWidth;
		this.noOfCols = noOfCols;
		this.noOfRows = noOfRows;
		this.contentStream = contentStream;
		this.singleRowSpan = tblHeight / noOfRows;
		this.singleColSpan = tblWidth / noOfCols;
	}

	public void drawTable() {

		String JsonFilePath = "src\\\\\\\\\\\\\\\\main\\\\\\\\\\\\\\\\resources\\\\\\\\\\\\\\\\assets\\\\\\\\\\\\\\\\test.txt";
		List<Map<String, String>> listOfCell = getCellJsonData(JsonFilePath);

		if (listOfCell != null) {
			this.calculateCellSeries(listOfCell, initX, initY, tblWidth, tblHeight);
		} else {
			System.out.println("can't Draw table");
		}

	}

	// Main and Complex Part of table
	public void calculateCellSeries(List<Map<String, String>> listOfCell, float initX, float initY, float tblWidth,
			float tblHeight) {
		float cellX = initX;
		float cellY = initY;
		float cellWidth = 0;
		float cellHeight = 0;

		float lowestHeight = 0;

		float endOfX = initX + tblWidth;
		float endOfY = initY - tblHeight;

		List<List<cellInfo>> allSeries = new ArrayList<List<cellInfo>>();
		List<cellInfo> singleSeries = new ArrayList<cellInfo>();

		for (Map<String, String> item : listOfCell) {
			
			cellWidth = Float.valueOf(item.get("colSpan")) * this.singleColSpan;
			cellHeight = Float.valueOf(item.get("rowSpan")) * this.singleRowSpan;
			
			// skipped for first time
			if (lowestHeight >= cellY - cellHeight && (singleSeries.size() != 0 ? (singleSeries.get(0).getY() <= cellY) : false)) { 
				cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"));

				singleSeries.add(cellinfo);
			}// allowed when cell height is less/above lowestHeight OR allowed on first time
			else if(lowestHeight <= cellY - cellHeight && singleSeries.size() == 0) { 
				cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"));

				singleSeries.add(cellinfo);
			}
			
			

			cellX += cellWidth;

			if (allSeries.size() != 0 && allSeries.get(allSeries.size() - 1).size() != 0) {
				for (cellInfo item1 : allSeries.get(allSeries.size() - 1)) {
					if (item1.getX() == cellX) {
						if (item1.getX() + item1.getHeight() < cellY) {
							cellY = item1.getX() + item1.getHeight();
						} else if (item1.getX() + item1.getHeight() > cellY) {

						} else {

						}
					}
				}
			}

			if (singleSeries.size() != 0) {
				lowestHeight = singleSeries.get(0).getY() + singleSeries.get(0).getHeight();
				if (singleSeries.size() >= 2) {
					for (int i = 2; i < singleSeries.size() + 1; i++) {
						if (lowestHeight < singleSeries.get(i - 1).getY() + singleSeries.get(i - 1).getHeight()) {
							lowestHeight = singleSeries.get(i - 1).getY() + singleSeries.get(i - 1).getHeight();
						}
					}
				}
			}

			// when its at end of X
			if (cellX == endOfX) {
				cellX = initX;
				cellY = singleSeries.get(0).getY() + singleSeries.get(0).getHeight();

				System.out.print("at the end of X");
				allSeries.add(singleSeries);
				singleSeries = new ArrayList<cellInfo>();
			}

		}

	}

	public void addCell(cellInfo cellData) throws IOException {
		float height = cellData.getHeight();
		float width = cellData.getWidth();
		float x = cellData.getX();
		float y = cellData.getY();

		this.contentStream.addRect(x, y, width, height);
		this.contentStream.setStrokingColor(Color.DARK_GRAY); // can be stored in cellInfo
		this.contentStream.setLineWidth(0.5f); // can be stored in cellInfo
		this.contentStream.stroke();

	}

	public List<Map<String, String>> getCellJsonData(String path) {
		List<Map<String, String>> listOfCell = new ArrayList<Map<String, String>>();

		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(path));
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray solutions = (JSONArray) jsonObject.get("cell");

			Iterator<Map<String, String>> iterator = (Iterator<Map<String, String>>) solutions.listIterator();

			float a = 0;

			while (iterator.hasNext()) {
				listOfCell.add(iterator.next());
			}

			return listOfCell;
		} catch (Exception | Error ex) {
			ex.printStackTrace();
			System.out.println("Issue while parsing JSON");
		}
		return null;
	}

}
