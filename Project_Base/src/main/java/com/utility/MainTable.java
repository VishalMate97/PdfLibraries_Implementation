package com.utility;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.text.DecimalFormat;

public class MainTable {
	private float initX;
	private float initY;
	private float tblWidth;
	private float tblHeight;
	private float noOfCols;
	private float noOfRows;
	private PDPageContentStream contentStream;
	
	private static final DecimalFormat df = new DecimalFormat("0.00");

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

	public void drawTable() throws IOException {

//		String JsonFilePath = "src\\\\\\\\\\\\\\\\main\\\\\\\\\\\\\\\\resources\\\\\\\\\\\\\\\\assets\\\\\\\\\\\\\\\\test.txt";
		String JsonFilePath = "src\\\\\\\\\\\\\\\\main\\\\\\\\\\\\\\\\resources\\\\\\\\\\\\\\\\assets\\\\\\\\\\\\\\\\test2.txt";

		List<List<cellInfo>> cellDrawList = new ArrayList<List<cellInfo>>();

		List<Map<String, String>> listOfCell = getCellJsonData(JsonFilePath);

		if (listOfCell != null) {
			cellDrawList = this.calculateCellSeries(listOfCell, initX, initY, tblWidth, tblHeight);
		} else {
			System.out.println("can't Draw table");
		}

		if (cellDrawList.size() != 0) {
			for (List<cellInfo> listData : cellDrawList) {
				for (cellInfo data : listData) {
					addCell(data);
				}
			}
		}

	}

	// Main and Complex Part of table
	public List<List<cellInfo>> calculateCellSeries(List<Map<String, String>> listOfCell, float initX, float initY,
			float tblWidth, float tblHeight) {
		float cellX = initX;
		float cellY = initY;
		float cellWidth = 0;
		float cellHeight = 0;

		float lowestHeight = 0;

		float endOfX = initX + tblWidth;
		float endOfY = initY - tblHeight;

		int counter = 0;

		List<List<cellInfo>> allSeries = new ArrayList<List<cellInfo>>();
		List<cellInfo> singleSeries = new ArrayList<cellInfo>();

		for (Map<String, String> item : listOfCell) {

			cellWidth = Float.valueOf(item.get("colSpan")) * this.singleColSpan;
			cellHeight = Float.valueOf(item.get("rowSpan")) * this.singleRowSpan;

			// below if else is --> to decide current cell should be added into this series
			if (Float.valueOf(df.format(lowestHeight)).floatValue() > Float.valueOf(df.format(cellY - cellHeight)).floatValue()
					&& (singleSeries.size() != 0 ? (Float.valueOf(df.format(singleSeries.get(0).getY())).floatValue() <= Float.valueOf(df.format(cellY)).floatValue()) : false)) {
				cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"));

				singleSeries.add(cellinfo);
			} // allowed when cell height is less/above lowestHeight OR allowed on first time
			else if (Float.valueOf(df.format(lowestHeight)).floatValue() <= Float.valueOf(df.format(cellY - cellHeight)).floatValue()) {
				cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"));

				singleSeries.add(cellinfo);
			}

			// when its at end of X
			if (Float.valueOf(df.format(cellX + cellWidth)).floatValue() >= Float.valueOf(df.format(endOfX)).floatValue()) {

				if (Float.valueOf(df.format(lowestHeight)).floatValue() < Float.valueOf(df.format(cellY - cellHeight)).floatValue()) {
					if (counter + 1 < listOfCell.size()
							? (Float.valueOf(df.format((cellY - cellHeight))).floatValue() - Float.valueOf(df.format((Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")).floatValue()
									* singleRowSpan)))) >= Float.valueOf(df.format(lowestHeight)).floatValue()
							: false) {
//									cellX = cellX - (Float.valueOf(listOfCell.get(counter + 1).get("colSpan")) * singleColSpan);
						cellX = cellX - cellWidth;
						cellY = cellY - cellHeight;
					}
				} else {
					cellX = initX;
					cellY = singleSeries.get(0).getY() - singleSeries.get(0).getHeight();

//					System.out.print("at the end of X");

					for (cellInfo item2 : singleSeries) {
						item2.setLowestHeight(lowestHeight);
					}

					allSeries.add(singleSeries);
					singleSeries = new ArrayList<cellInfo>();
					lowestHeight = 0;
					continue;
				}

			}

			if (singleSeries.size() == 1) {
				cellX += cellWidth;
			}

			boolean xFOund = false;
			// will be skipped for first series only
			// will be checked every time (to check is ther any space in above series)
			if (allSeries.size() != 0 && allSeries.get(allSeries.size() - 1).size() != 0) {

				for (cellInfo item1 : allSeries.get(allSeries.size() - 1)) {
					if (Float.valueOf(df.format(item1.getX())).floatValue() == Float.valueOf(df.format(cellX)).floatValue()) {
						if (Float.valueOf(df.format(item1.getY())).floatValue() - Float.valueOf(df.format(item1.getHeight())).floatValue() > Float.valueOf(df.format(cellY)).floatValue()) {
							cellY = item1.getY() - item1.getHeight();
							xFOund = true;
						}

					}
				}
//				if (!xFOund) {
//					myLoop2: for (int l = allSeries.get(allSeries.size() - 1).size() - 1; l >= 0; l--) {
//						if (Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getX())).floatValue() < Float.valueOf(df.format(cellX)).floatValue()
//								&& Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l).getX())).floatValue() > Float.valueOf(df.format(cellX)).floatValue()) {
//
//							if (Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1)
//									.getLowestHeight())).floatValue() < Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY())).floatValue()
//											- Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getHeight())).floatValue()) {
//								cellY = Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY())).floatValue()
//										- Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getHeight())).floatValue();
//							} else if (Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1)
//									.getLowestHeight())).floatValue() == Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY())).floatValue()
//											- Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getHeight())).floatValue()) {
//								if(Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getX())).floatValue()
//										- Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getWidth())).floatValue() <= Float.valueOf(df.format(cellX)).floatValue()) {
//									cellY = Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY())).floatValue();
//								}
//							}
//							break myLoop2;
//						}
//					}
//				}

			}

			// we are setting lowest height
			if (singleSeries.size() != 0) {
				lowestHeight = Float.valueOf(df.format(singleSeries.get(0).getY())).floatValue() - Float.valueOf(df.format(singleSeries.get(0).getHeight())).floatValue();
				if (singleSeries.size() >= 2) { // size should be
					for (int i = 1; i < singleSeries.size(); i++) {
						if (Float.valueOf(df.format(lowestHeight)).floatValue() > Float.valueOf(df.format(singleSeries.get(i).getY())).floatValue() - Float.valueOf(df.format(singleSeries.get(i).getHeight())).floatValue()) {
							lowestHeight = singleSeries.get(i).getY() - singleSeries.get(i).getHeight();
						}
						if (i == singleSeries.size() - 1
								&& Float.valueOf(df.format(lowestHeight)).floatValue() >= Float.valueOf(df.format(singleSeries.get(singleSeries.size() - 1).getY())).floatValue()
										- Float.valueOf(df.format(singleSeries.get(singleSeries.size() - 1).getHeight())).floatValue()
								&& Float.valueOf(df.format(cellX + cellWidth)).floatValue() != Float.valueOf(df.format(endOfX)).floatValue()) {

							// to set next cell's X and Y if current Cell's Y-height less than lowest
							innerLoop: // this is only for first series
							for (int k = 0; k < singleSeries.size(); k++) {
								if (Float.valueOf(df.format(singleSeries.get(k).getX())).floatValue() == Float.valueOf(df.format(singleSeries.get(singleSeries.size() - 1).getX())).floatValue()) {
									cellX = singleSeries.get(k).getX() + singleSeries.get(k).getWidth();
									cellY = singleSeries.get(k).getY();
									break;
								}
							}

						} else if (i == singleSeries.size() - 1
								&& Float.valueOf(df.format(lowestHeight)).floatValue() <= Float.valueOf(df.format(singleSeries.get(singleSeries.size() - 1).getY())).floatValue()
										- Float.valueOf(df.format(singleSeries.get(singleSeries.size() - 1).getHeight())).floatValue()
								&& Float.valueOf(df.format(cellX + cellWidth)) != Float.valueOf(df.format(endOfX)).floatValue()) {

							// to set next cell's X and Y if current Cell's Y-height greater than lowest
							if (counter + 1 < listOfCell.size() ? (Float.valueOf(df.format(lowestHeight)).floatValue() <= Float.valueOf(df.format((cellY - cellHeight))).floatValue()
									- Float.valueOf(df.format((Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan))).floatValue())
									: false) {
//								cellX = cellX - (Float.valueOf(listOfCell.get(counter + 1).get("colSpan")) * singleColSpan);
//								

								for (int k = 0; k < singleSeries.size(); k++) {
									if (Float.valueOf(df.format(singleSeries.get(k).getX())).floatValue() == Float.valueOf(df.format(cellX)).floatValue()) {
										cellX = singleSeries.get(k).getX();
										cellY = singleSeries.get(k).getY() - singleSeries.get(k).getHeight();
									}
								}
							} else if (counter + 1 < listOfCell.size() ? (Float.valueOf(df.format(lowestHeight)).floatValue() >= Float.valueOf(df.format((cellY - cellHeight))).floatValue()
									- Float.valueOf(df.format((Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan))).floatValue())
									: false) {

								myloop: for (int k = 0; k < singleSeries.size(); k++) {
									if (Float.valueOf(df.format(singleSeries.get(k).getX())) == Float.valueOf(df.format(cellX)).floatValue()) {
										cellX = cellX + singleSeries.get(k).getWidth();
										cellY = cellY + singleSeries.get(k).getHeight();
										break myloop;
									}
								}

							}
						}
					}
				}
			}

			counter++;

		}

		return allSeries;

	}

	public void addCell(cellInfo cellData) throws IOException {
		float height = cellData.getHeight();
		float width = cellData.getWidth();
		float x = cellData.getX();
		float y = cellData.getY();

		this.contentStream.addRect(x, y, width, -height);
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
