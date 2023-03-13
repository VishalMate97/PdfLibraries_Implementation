package com.utility;

import java.awt.Color;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.temp.myPdfDynamicLogic;

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
		
		myPdfDynamicLogic temp = new myPdfDynamicLogic();

		if (listOfCell != null) {
//			cellDrawList = this.calculateCellSeries(listOfCell, initX, initY, tblWidth, tblHeight);
			cellDrawList = temp.calculateCellSeries(listOfCell, initX, initY, tblWidth, tblHeight, this.singleColSpan, this.singleRowSpan);
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
		float cellX = Float.valueOf(df.format(initX));
		float cellY = Float.valueOf(df.format(initY));
		float cellWidth = 0;
		float cellHeight = 0;
		float cellEndX = 0;
		float cellEndY = 0;
		
		float lowestHeight = 0;

		float endOfX = Float.valueOf(df.format(initX + tblWidth));
		float endOfY = Float.valueOf(df.format(initY - tblHeight));

		int counter = 0;

		List<List<cellInfo>> allSeries = new ArrayList<List<cellInfo>>();
		List<cellInfo> singleSeries = new ArrayList<cellInfo>();

		for (Map<String, String> item : listOfCell) {

			cellWidth = Float.valueOf(df.format(Float.valueOf(item.get("colSpan")) * this.singleColSpan));
			cellHeight = Float.valueOf(df.format(Float.valueOf(item.get("rowSpan")) * this.singleRowSpan));

			cellEndX = cellX + cellWidth;
			cellEndY = cellY - cellHeight;
			
			// below if else is --> to decide current cell should be added into this series
			if (lowestHeight  > cellY - cellHeight 
					&& (singleSeries.size() != 0 ? (singleSeries.get(0).getY()  <= cellY ) : false)) {
				cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"), cellEndX, cellEndY);

				singleSeries.add(cellinfo);
			} // allowed when cell height is less/above lowestHeight OR allowed on first time
			else if (lowestHeight  <= cellY - cellHeight ) {
				cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"), cellEndX, cellEndY);

				singleSeries.add(cellinfo);
			}

			// when its at end of X
			if (cellX + cellWidth  >= endOfX ) {

				if (lowestHeight  < cellY - cellHeight ) {
					if (counter + 1 < listOfCell.size()
							? ((cellY - cellHeight)  - (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * this.singleRowSpan)) >= lowestHeight 
							: false) {
//									cellX = cellX - (Float.valueOf(listOfCell.get(counter + 1).get("colSpan")) * singleColSpan);
						cellX = Float.valueOf(df.format(cellX - cellWidth));
						cellY = Float.valueOf(df.format(cellY - cellHeight));
					}
				} else {
					cellX = Float.valueOf(df.format(initX));
					cellY = Float.valueOf(df.format(singleSeries.get(0).getY() - singleSeries.get(0).getHeight()));

//					System.out.print("at the end of X");

					for (cellInfo item2 : singleSeries) {
						item2.setLowestHeight(Float.valueOf(df.format(lowestHeight)));
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
					if ( item1.getX()  ==  cellX ) {
						if ( item1.getY()  -  item1.getHeight()  >  cellY ) {
							cellY = Float.valueOf(df.format(item1.getY() - item1.getHeight()));
							xFOund = true;
						}

					}
				}
//				if (!xFOund) {
//					myLoop2: for (int l = allSeries.get(allSeries.size() - 1).size() - 1; l >= 0; l--) {
//						if ( allSeries.get(allSeries.size() - 1).get(l - 1).getX()  <  cellX 
//								&&  allSeries.get(allSeries.size() - 1).get(l).getX()  >  cellX ) {
//
//							if ( allSeries.get(allSeries.size() - 1).get(l - 1)
//									.getLowestHeight()  <  allSeries.get(allSeries.size() - 1).get(l - 1).getY() 
//											-  allSeries.get(allSeries.size() - 1).get(l - 1).getHeight() ) {
//								cellY =  Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY() 
//										-  allSeries.get(allSeries.size() - 1).get(l - 1).getHeight())) ;
//							} else if ( allSeries.get(allSeries.size() - 1).get(l - 1)
//									.getLowestHeight()  ==  allSeries.get(allSeries.size() - 1).get(l - 1).getY() 
//											-  allSeries.get(allSeries.size() - 1).get(l - 1).getHeight() ) {
//								if( allSeries.get(allSeries.size() - 1).get(l - 1).getX() 
//										-  allSeries.get(allSeries.size() - 1).get(l - 1).getWidth()  <=  cellX ) {
//									cellY =  Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY())) ;
//								}
//							}
//							break myLoop2;
//						}
//					}
//				}

			}

			// we are setting lowest height
			if (singleSeries.size() != 0) {
				lowestHeight =  Float.valueOf(df.format(singleSeries.get(0).getY()  -  singleSeries.get(0).getHeight())) ;
				if (singleSeries.size() >= 2) { // size should be
					for (int i = 1; i < singleSeries.size(); i++) {
						if ( lowestHeight  >  singleSeries.get(i).getY()  -  singleSeries.get(i).getHeight() ) {
							lowestHeight = Float.valueOf(df.format(singleSeries.get(i).getY() - singleSeries.get(i).getHeight()));
						}
						if (i == singleSeries.size() - 1
								&&  lowestHeight  >=  singleSeries.get(singleSeries.size() - 1).getY() 
										-  singleSeries.get(singleSeries.size() - 1).getHeight() 
								&&  cellX + cellWidth  !=  endOfX ) {

							// to set next cell's X and Y if current Cell's Y-height less than lowest
							innerLoop: // this is only for first series
							for (int k = 0; k < singleSeries.size(); k++) {
								if ( singleSeries.get(k).getX()  ==  singleSeries.get(singleSeries.size() - 1).getX() ) {
									cellX = Float.valueOf(df.format(singleSeries.get(k).getX() + singleSeries.get(k).getWidth()));
									cellY = Float.valueOf(df.format(singleSeries.get(k).getY()));
									break;
								}
							}

						} else if (i == singleSeries.size() - 1
								&&  lowestHeight  <=  singleSeries.get(singleSeries.size() - 1).getY() 
										-  singleSeries.get(singleSeries.size() - 1).getHeight() 
								&&  (cellX + cellWidth) !=  endOfX ) {

							// to set next cell's X and Y if current Cell's Y-height greater than lowest
							if (counter + 1 < listOfCell.size() ? ( lowestHeight  <=  (cellY - cellHeight) 
									-  (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan) )
									: false) {
//								cellX = cellX - (Float.valueOf(listOfCell.get(counter + 1).get("colSpan")) * singleColSpan);
//								

								for (int k = 0; k < singleSeries.size(); k++) {
									if ( singleSeries.get(k).getX()  ==  cellX ) {
										cellX = Float.valueOf(df.format(singleSeries.get(k).getX()));
										cellY = Float.valueOf(df.format(singleSeries.get(k).getY() - singleSeries.get(k).getHeight()));
									}
								}
							} else if (counter + 1 < listOfCell.size() ? ( lowestHeight  >=  (cellY - cellHeight) 
									-  (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan) )
									: false) {

								myloop: for (int k = 0; k < singleSeries.size(); k++) {
									if ( singleSeries.get(k).getX() ==  cellX ) {
										cellX = Float.valueOf(df.format(cellX + singleSeries.get(k).getWidth()));
										cellY = Float.valueOf(df.format(cellY + singleSeries.get(k).getHeight()));
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
