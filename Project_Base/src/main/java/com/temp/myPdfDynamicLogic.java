package com.temp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.utility.cellInfo;

public class myPdfDynamicLogic {
	
	private static final DecimalFormat df = new DecimalFormat("0.00");

	// Main and Complex Part of table
		public List<List<cellInfo>> calculateCellSeries(List<Map<String, String>> listOfCell, float initX, float initY,
				float tblWidth, float tblHeight, float  singleColSpan, float singleRowSpan) {
			float cellX = Float.valueOf(df.format(initX));
			float cellY = Float.valueOf(df.format(initY));
			float cellWidth = 0;
			float cellHeight = 0;
			float cellEndX = 0;
			float cellEndY = 0;

			cellInfo currentCell;
			
			float lowestHeight = 0;

			float endOfX = Float.valueOf(df.format(initX + tblWidth));
			float endOfY = Float.valueOf(df.format(initY - tblHeight));

			int counter = 0;

			List<List<cellInfo>> allSeries = new ArrayList<List<cellInfo>>();
			List<cellInfo> singleSeries = new ArrayList<cellInfo>();

			mainLoop1:
			for (Map<String, String> item : listOfCell) {

				cellWidth = Float.valueOf(df.format(Float.valueOf(item.get("colSpan")) * singleColSpan));
				cellHeight = Float.valueOf(df.format(Float.valueOf(item.get("rowSpan")) * singleRowSpan));

				cellEndX = cellX + cellWidth;
				cellEndY = cellY - cellHeight;
				
				// below if else is --> to decide current cell should be added into this series
				if (lowestHeight  > cellEndY 
						&& (singleSeries.size() != 0 ? (singleSeries.get(0).getY()  <= cellY ) : false)) {
					cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"), cellEndX, cellEndY);

					singleSeries.add(cellinfo);
				} // allowed when cell height is less/above lowestHeight OR allowed on first time
				else if (lowestHeight  <= cellEndY ) {
					cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"), cellEndX, cellEndY);

					singleSeries.add(cellinfo);
				}

				currentCell = singleSeries.get(singleSeries.size() - 1);
				
				// when its at end of X
				if (currentCell.getCellEndX()  >= endOfX ) {

					if (lowestHeight  < currentCell.getCellEndY() ) {
						if (counter + 1 < listOfCell.size()
								? (currentCell.getCellEndY()  - (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan)) >= lowestHeight 
								: false) {
//										cellX = cellX - (Float.valueOf(listOfCell.get(counter + 1).get("colSpan")) * singleColSpan);
							cellX = Float.valueOf(df.format(currentCell.getX() - currentCell.getWidth()));
							cellY = Float.valueOf(df.format(currentCell.getCellEndY()));
//							continue mainLoop1;
						}
					} else {
						cellX = Float.valueOf(df.format(initX));
						cellY = Float.valueOf(df.format(singleSeries.get(0).getY() - singleSeries.get(0).getHeight()));

//						System.out.print("at the end of X");

						for (cellInfo item2 : singleSeries) {
							item2.setLowestHeight(Float.valueOf(df.format(lowestHeight)));
						}

						allSeries.add(singleSeries);
						singleSeries = new ArrayList<cellInfo>();
						lowestHeight = 0;
						continue mainLoop1;
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
						if ( item1.getX()  ==  currentCell.getCellEndX() ) {
							if ( item1.getY()  -  item1.getHeight()  >  currentCell.getY() ) {
								cellY = Float.valueOf(df.format(item1.getY() - item1.getHeight()));
								xFOund = true;
//								continue mainLoop1;
							}

						}
					}
					if (!xFOund) {
						myLoop2: for (int l = allSeries.get(allSeries.size() - 1).size() - 1; l > 0; l--) {
							if ( allSeries.get(allSeries.size() - 1).get(l - 1).getX()  <  currentCell.getX() 
									&&  allSeries.get(allSeries.size() - 1).get(l).getX()  >  currentCell.getX()  ) {
	
								if ( allSeries.get(allSeries.size() - 1).get(l - 1)
										.getLowestHeight()  <  allSeries.get(allSeries.size() - 1).get(l - 1).getY() 
												-  allSeries.get(allSeries.size() - 1).get(l - 1).getHeight() ) {
									cellY =  Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY() 
											-  allSeries.get(allSeries.size() - 1).get(l - 1).getHeight())) ;
								} 
								else if ( allSeries.get(allSeries.size() - 1).get(l - 1)
										.getLowestHeight()  ==  allSeries.get(allSeries.size() - 1).get(l - 1).getY() 
												-  allSeries.get(allSeries.size() - 1).get(l - 1).getHeight() ) {
									if( allSeries.get(allSeries.size() - 1).get(l - 1).getX() 
											-  allSeries.get(allSeries.size() - 1).get(l - 1).getWidth()  <=  currentCell.getX() ) {
										cellY =  Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY())) ;
									}
								}
								break myLoop2;
//								continue mainLoop1;
							}
						}
					}

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
									&&  currentCell.getCellEndX()  !=  endOfX ) {

								// to set next cell's X and Y if current Cell's Y-height less than lowest
								innerLoop: // this is only for first series
								for (int k = 0; k < singleSeries.size(); k++) {
									if ( singleSeries.get(k).getX()  ==  singleSeries.get(singleSeries.size() - 1).getX() ) {
										cellX = Float.valueOf(df.format(singleSeries.get(k).getX() + singleSeries.get(k).getWidth()));
										cellY = Float.valueOf(df.format(singleSeries.get(k).getY()));
										break;
//										continue mainLoop1;
									}
								}

							} else if (i == singleSeries.size() - 1
									&&  lowestHeight  <=  singleSeries.get(singleSeries.size() - 1).getY() 
											-  singleSeries.get(singleSeries.size() - 1).getHeight() 
									&&  currentCell.getCellEndX() !=  endOfX ) {

								// to set next cell's X and Y if current Cell's Y-height greater than lowest
								if (counter + 1 < listOfCell.size() ? ( lowestHeight  <=  currentCell.getCellEndY()
										-  (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan) )
										: false) {
//									cellX = cellX - (Float.valueOf(listOfCell.get(counter + 1).get("colSpan")) * singleColSpan);
//									

									for (int k = 0; k < singleSeries.size(); k++) {
										if ( singleSeries.get(k).getX()  ==  currentCell.getX() ) {
											cellX = Float.valueOf(df.format(singleSeries.get(k).getX()));
											cellY = Float.valueOf(df.format(singleSeries.get(k).getY() - singleSeries.get(k).getHeight()));
//											continue mainLoop1;
										}
									}
								} else if (counter + 1 < listOfCell.size() ? ( lowestHeight  >=  currentCell.getCellEndY() 
										-  (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan) )
										: false) {

									myloop: for (int k = 0; k < singleSeries.size(); k++) {
										if ( singleSeries.get(k).getX() ==  currentCell.getX() ) {
											cellX = Float.valueOf(df.format(currentCell.getX() + singleSeries.get(k).getWidth()));
											cellY = Float.valueOf(df.format(currentCell.getY() + singleSeries.get(k).getHeight()));
											break myloop;
//											continue mainLoop1;
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

	
}
