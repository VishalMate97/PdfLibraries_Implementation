package com.temp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import com.utility.cellInfo;

public class myPdfDynamicLogic {

	private static final DecimalFormat df = new DecimalFormat("0.00");

	// Main and Complex Part of table
	public List<List<cellInfo>> calculateCellSeries1(List<Map<String, String>> listOfCell, float initX, float initY,
			float tblWidth, float tblHeight, float singleColSpan, float singleRowSpan) {
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

		mainLoop1: for (Map<String, String> item : listOfCell) {

			cellWidth = Float.valueOf(df.format(Float.valueOf(item.get("colSpan")) * singleColSpan));
			cellHeight = Float.valueOf(df.format(Float.valueOf(item.get("rowSpan")) * singleRowSpan));

			cellEndX = cellX + cellWidth;
			cellEndY = cellY - cellHeight;

			// below if else is --> to decide current cell should be added into this series
			if (lowestHeight > cellEndY && (singleSeries.size() != 0 ? (singleSeries.get(0).getY() <= cellY) : false)) {
				cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"), cellEndX,
						cellEndY);

				singleSeries.add(cellinfo);
			} // allowed when cell height is less/above lowestHeight OR allowed on first time
			else if (lowestHeight <= cellEndY) {
				cellInfo cellinfo = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"), cellEndX,
						cellEndY);

				singleSeries.add(cellinfo);
			}

			currentCell = singleSeries.get(singleSeries.size() - 1);

			// when its at end of X
			if (currentCell.getCellEndX() >= endOfX) {

				if (lowestHeight < currentCell.getCellEndY()) {
					if (counter + 1 < listOfCell.size()
							? (currentCell.getCellEndY() - (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan"))
									* singleRowSpan)) >= lowestHeight
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
					if (item1.getX() == currentCell.getCellEndX()) {
						if (item1.getY() - item1.getHeight() > currentCell.getY()) {
							cellY = Float.valueOf(df.format(item1.getY() - item1.getHeight()));
							xFOund = true;
//								continue mainLoop1;
						}

					}
				}
				if (!xFOund) {
					myLoop2: for (int l = allSeries.get(allSeries.size() - 1).size() - 1; l > 0; l--) {
						if (allSeries.get(allSeries.size() - 1).get(l - 1).getX() < currentCell.getX()
								&& allSeries.get(allSeries.size() - 1).get(l).getX() > currentCell.getX()) {

							if (allSeries.get(allSeries.size() - 1).get(l - 1)
									.getLowestHeight() < allSeries.get(allSeries.size() - 1).get(l - 1).getY()
											- allSeries.get(allSeries.size() - 1).get(l - 1).getHeight()) {
								cellY = Float.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY()
										- allSeries.get(allSeries.size() - 1).get(l - 1).getHeight()));
							} else if (allSeries.get(allSeries.size() - 1).get(l - 1)
									.getLowestHeight() == allSeries.get(allSeries.size() - 1).get(l - 1).getY()
											- allSeries.get(allSeries.size() - 1).get(l - 1).getHeight()) {
								if (allSeries.get(allSeries.size() - 1).get(l - 1).getX() - allSeries
										.get(allSeries.size() - 1).get(l - 1).getWidth() <= currentCell.getX()) {
									cellY = Float
											.valueOf(df.format(allSeries.get(allSeries.size() - 1).get(l - 1).getY()));
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
				lowestHeight = Float.valueOf(df.format(singleSeries.get(0).getY() - singleSeries.get(0).getHeight()));
				if (singleSeries.size() >= 2) { // size should be
					for (int i = 1; i < singleSeries.size(); i++) {
						if (lowestHeight > singleSeries.get(i).getY() - singleSeries.get(i).getHeight()) {
							lowestHeight = Float
									.valueOf(df.format(singleSeries.get(i).getY() - singleSeries.get(i).getHeight()));
						}
						if (i == singleSeries.size() - 1
								&& lowestHeight >= singleSeries.get(singleSeries.size() - 1).getY()
										- singleSeries.get(singleSeries.size() - 1).getHeight()
								&& currentCell.getCellEndX() != endOfX) {

							// to set next cell's X and Y if current Cell's Y-height less than lowest
							innerLoop: // this is only for first series
							for (int k = 0; k < singleSeries.size(); k++) {
								if (singleSeries.get(k).getX() == singleSeries.get(singleSeries.size() - 1).getX()) {
									cellX = Float.valueOf(
											df.format(singleSeries.get(k).getX() + singleSeries.get(k).getWidth()));
									cellY = Float.valueOf(df.format(singleSeries.get(k).getY()));
									break;
//										continue mainLoop1;
								}
							}

						} else if (i == singleSeries.size() - 1
								&& lowestHeight <= singleSeries.get(singleSeries.size() - 1).getY()
										- singleSeries.get(singleSeries.size() - 1).getHeight()
								&& currentCell.getCellEndX() != endOfX) {

							// to set next cell's X and Y if current Cell's Y-height greater than lowest
							if (counter + 1 < listOfCell.size() ? (lowestHeight <= currentCell.getCellEndY()
									- (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan))
									: false) {
//									cellX = cellX - (Float.valueOf(listOfCell.get(counter + 1).get("colSpan")) * singleColSpan);
//									

								for (int k = 0; k < singleSeries.size(); k++) {
									if (singleSeries.get(k).getX() == currentCell.getX()) {
										cellX = Float.valueOf(df.format(singleSeries.get(k).getX()));
										cellY = Float.valueOf(df
												.format(singleSeries.get(k).getY() - singleSeries.get(k).getHeight()));
//											continue mainLoop1;
									}
								}
							} else if (counter + 1 < listOfCell.size() ? (lowestHeight >= currentCell.getCellEndY()
									- (Float.valueOf(listOfCell.get(counter + 1).get("rowSpan")) * singleRowSpan))
									: false) {

								myloop: for (int k = 0; k < singleSeries.size(); k++) {
									if (singleSeries.get(k).getX() == currentCell.getX()) {
										cellX = Float.valueOf(
												df.format(currentCell.getX() + singleSeries.get(k).getWidth()));
										cellY = Float.valueOf(
												df.format(currentCell.getY() + singleSeries.get(k).getHeight()));
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

	// Main and Complex Part of table
	public List<List<cellInfo>> calculateCellSeries2(List<Map<String, String>> listOfCell, float initX, float initY,
			float tblWidth, float tblHeight, float singleColSpan, float singleRowSpan) {
		float cellX = Float.valueOf(df.format(initX));
		float cellY = Float.valueOf(df.format(initY));
		float cellWidth = 0;
		float cellHeight = 0;
		float cellEndX = 0;
		float cellEndY = 0;

		cellInfo recentCell = null;
		cellInfo currentCell = null;
		cellInfo nextCell = null;

		float lowestHeight = 0;
		float highestHeight = 0; // it will always X of first cell of current series array

		float endOfX = Float.valueOf(df.format(initX + tblWidth));
		float endOfY = Float.valueOf(df.format(initY - tblHeight));

		int counter = 0; // to count the iteration of Actual listOfCell

		List<List<cellInfo>> allSeries = new ArrayList<List<cellInfo>>();
		List<cellInfo> singleSeries = new ArrayList<cellInfo>();

		for (Map<String, String> item : listOfCell) {

			// getting recently added cell, If the size of singleSeries if greater or equal
			// to 1
			recentCell = singleSeries.size() >= 1 ? singleSeries.get(singleSeries.size() - 1) : null;

			// -----------------------------------------------------------------------

			// If recent cell's cellEndX reach to the end of table(horizontally endOfX)
			if (singleSeries.size() >= 1 ? recentCell.getCellEndX() == endOfX : false) {

				cellX = Float.valueOf(df.format(initX));
				cellY = Float.valueOf(df.format(singleSeries.get(0).getCellEndY()));

				for (cellInfo item2 : singleSeries) {
					item2.setLowestHeight(Float.valueOf(df.format(lowestHeight)));
				}

				allSeries.add(singleSeries);
				singleSeries = new ArrayList<cellInfo>();
				lowestHeight = 0;
				highestHeight = 0;
			}

			// -----------------------------------------------------------------------

			// calculate cellHeight and cellWidth on 'every iteration'
			cellWidth = Float.valueOf(df.format(Float.valueOf(item.get("colSpan")) * singleColSpan));
			cellHeight = Float.valueOf(df.format(Float.valueOf(item.get("rowSpan")) * singleRowSpan));

			// EndX and EndY should be initialized for 'first' cellX and cellY
			if (singleSeries.size() == 0) {
				cellEndX = Float.valueOf(df.format(cellX + cellWidth));// temporarily blocked
				cellEndY = Float.valueOf(df.format(cellY - cellHeight));// temporarily blocked
			}

			// -----------------------------------------------------------------------

			if (singleSeries.size() == 0) {
				highestHeight = cellX;
				lowestHeight = cellEndY;
			} else if (singleSeries.size() >= 2) { // we are setting size >= 2, because at 0 size we can't, on 1 size
													// there is only one lowestHeight, but on 2 size we can compare
				lowestHeight = lowestHeight > recentCell.getCellEndY() ? recentCell.getCellEndY() : lowestHeight;
			}

			// -----------------------------------------------------------------------

			// EndX and EndY should be initialized for 'first' cellX and cellY
			if (singleSeries.size() == 0) {
				currentCell = new cellInfo(cellX, cellY, cellWidth, cellHeight, item.get("cellNo"), cellEndX, cellEndY);
			} else if (singleSeries.size() >= 1) {
				currentCell = getNextCellInfo(item, recentCell, lowestHeight, highestHeight, singleColSpan,
						singleRowSpan, singleSeries, allSeries);// temporarily blocked // main patch
			}

			singleSeries.add(currentCell);

			// -----------------------------------------------------------------------

			counter++;

			// -----------------------------------------------------------------------

			// not able to add singleSeries at the very end of table,
			// coz execution pointer get out of forloop,
			// and didn't able to check recent cell is at end of table X
			// solution - we will copy whole logic here - when counter is equal to size of
			// listCell
			if (counter == listOfCell.size()) {
				cellX = Float.valueOf(df.format(initX));
				cellY = Float.valueOf(df.format(singleSeries.get(0).getCellEndY()));

				for (cellInfo item2 : singleSeries) {
					item2.setLowestHeight(Float.valueOf(df.format(lowestHeight)));
				}

				allSeries.add(singleSeries);
				singleSeries = new ArrayList<cellInfo>();
				lowestHeight = 0;
				highestHeight = 0;
			}

		}

		return allSeries;
	}

	cellInfo getNextCellInfo(Map<String, String> data, cellInfo recentCell, float lowestHeight, float highestHeight,
			float singleColSpan, float singleRowSpan, List<cellInfo> singleSeries, List<List<cellInfo>> allSeries) {

		float X = 0;
		float Y = 0;
		float EndX = 0;
		float EndY = 0;
		float height = 0;
		float width = 0;

		width = Float.valueOf(df.format(Float.valueOf(data.get("colSpan")) * singleColSpan));
		height = Float.valueOf(df.format(Float.valueOf(data.get("rowSpan")) * singleRowSpan));

		// -----------------------------------------------------------------------

		// Calculate X and Y

		/*
		 * just calculating X and Y trying below logic for simpler example only to get
		 * idea so there are two flows - one for X and one for Y
		 */

		// working for first series
		if (allSeries.size() == 0) {

			// calculating for 2nd cell
			if (singleSeries.size() == 1) {
				X = Float.valueOf(df.format(recentCell.getCellEndX()));
				Y = Float.valueOf(df.format(recentCell.getY()));
				return getSingleCellInfo(X, Y, width, height, data.get("cellNo"));
			}

			// if new cell fit above lowestHeight
			if (recentCell.getCellEndY() - height >= lowestHeight) {
				X = Float.valueOf(df.format(recentCell.getX()));
				Y = Float.valueOf(df.format(recentCell.getCellEndY()));
				return getSingleCellInfo(X, Y, width, height, data.get("cellNo"));
			} 
			else 
			{
				// two cases for this else -
				// 1. check is there any space available infront of recentCell(which is under another cell) and below another cell(which is above recent cell)
				// 2. another case is to move from above option 1. to next cell
				
				
				cellInfo firstCellOfSeries = singleSeries.get(0); // get first cell of current series

				// get all cell's whose X axis is same as recent cell's X axis, 
				// and take a cell whose width is maximum between all filtered cells
				cellInfo cellWithMaxWidthOnSameX = singleSeries.stream().
						filter(i -> i.getX() == recentCell.getX())
						.max(Comparator.comparing(cellInfo::getWidth))
						.orElseThrow(NoSuchElementException::new);
				
				// check is there any space available infront of recentCell and above cell
				if (recentCell.getCellEndX() < cellWithMaxWidthOnSameX.getCellEndX()) {

					// again if new cell fit above lowestHieght
					if(cellWithMaxWidthOnSameX.getCellEndY() - height >= lowestHeight) {
						X = Float.valueOf(df.format(recentCell.getCellEndX()));
						Y = Float.valueOf(df.format(cellWithMaxWidthOnSameX.getCellEndY()));
						return getSingleCellInfo(X, Y, width, height, data.get("cellNo"));
					}
					
					
				} 
				else 
				{

					// below for and nested if condition is checking 
					// all cell's whose X axis is same as recent cell's X axis, AND Y axis same as first cell's Y axis
					// below 'if' will work if we have a recent cell's X axis, in cells of current series
					for (cellInfo tempCell1 : singleSeries) {
						if (recentCell.getX() == tempCell1.getX() && firstCellOfSeries.getY() == tempCell1.getY()) {
							X = tempCell1.getCellEndX();
						}
					}
					
					// If we didn't get any value for X from above 'if'
					// if recent cell's X is between another cell
					// then get that cell's endX as current X
					if(X == 0 ) {
						X = singleSeries.stream().
						filter(i -> i.getX() < recentCell.getX() && i.getCellEndX() > recentCell.getX())
						.max(Comparator.comparing(cellInfo::getWidth))
						.orElse(null).getCellEndX();
						
						
					}
					
					// as its first series we can use Y axis of first cell
					Y = Float.valueOf(df.format(firstCellOfSeries.getY()));
					
					// we got both X and Y
					return getSingleCellInfo(X, Y, width, height, data.get("cellNo"));
				}
			}

		} else // other than first series
		{
			X = Float.valueOf(df.format(recentCell.getCellEndX()));
			Y = Float.valueOf(df.format(recentCell.getY()));
		}

		// -----------------------------------------------------------------------

		/* after calculating X and Y just calculate EndX, EndY */
		EndX = Float.valueOf(df.format(X + width));
		EndY = Float.valueOf(df.format(Y - height));

		return new cellInfo(X, Y, width, height, data.get("cellNo"), EndX, EndY);
	}

	public cellInfo getSingleCellInfo(float X, float Y, float width, float height, String cellNo) {

		float EndX = Float.valueOf(df.format(X + width));
		float EndY = Float.valueOf(df.format(Y - height));

		return new cellInfo(X, Y, width, height, cellNo, EndX, EndY);
	}

	// simpler tables
//	cellInfo getNextCellInfo(Map<String,String> data,cellInfo recentCell, float lowestHeight, float highestHeight, float singleColSpan, float singleRowSpan){
//		
//		float X;
//		float Y;
//		float EndX;
//		float EndY;
//		float height;
//		float width;
//		
//		width = Float.valueOf(df.format(Float.valueOf(data.get("colSpan")) * singleColSpan));
//		height = Float.valueOf(df.format(Float.valueOf(data.get("rowSpan")) * singleRowSpan));
//		
//		//-----------------------------------------------------------------------
//		
//		 /* just calculating X and Y 
//		 * 	trying below logic for simpler example only to get idea
//		 * 	so there are two flows - one for X and one for Y
//		 */
//		X = Float.valueOf(df.format(recentCell.getX() + recentCell.getWidth()));
//		Y = Float.valueOf(df.format(recentCell.getY()));
//		
//		
//		//-----------------------------------------------------------------------
//		
//		 /* after calculating X and Y just calculate EndX, EndY*/
//		EndX = Float.valueOf(df.format(X + width));
//		EndY = Float.valueOf(df.format(Y + height));
//		
//		
//		return new cellInfo(X, Y, width, height, data.get("cellNo"), EndX, EndY);
//	}
}
