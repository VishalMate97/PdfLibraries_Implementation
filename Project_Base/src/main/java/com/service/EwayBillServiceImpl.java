package com.service;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import com.itextpdf.text.pdf.PdfPage;
import com.model.columnInfo;
import com.model.rowInfo;

public class EwayBillServiceImpl implements EwayBillService {
	List<rowInfo> part1;
	List<columnInfo> part1ColumnWidth;
	List<rowInfo> part2A;
	List<columnInfo> part2AColumnWidth;
	List<Map<String, String>> part3B;
	List<columnInfo> part3BColumnWidth;

	public void start() throws IOException {

		this.initializeData();

		PDDocument document = new PDDocument();
		PDPage page = new PDPage(); // creating page with size(8.5 * 11 inches) --> width = 612, height = 792
		document.addPage(page);

		float pageHeight = page.getTrimBox().getHeight();
		float pageWidth = page.getTrimBox().getWidth();

		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// text Section
		String text = "Hello All";
		PDFont font = PDType1Font.TIMES_ROMAN;
		float fontSize = 7.5f;
		float initX = 50;
		float initY = pageHeight - 50;
		float leading = 10;

		float updatedTotalHeight = 0;

//		this.createSingleLineText(contentStream,text,leading,x,y,font,fontSize);

		this.createSingleLineText(contentStream, "E-Way Bill", 10,
				100 + ((pageWidth - 200) / 2) - (int) (font.getStringWidth("E-Way Bill") / 1000 * 10), initY,
				PDType1Font.HELVETICA_BOLD, 10);

		// table Section
//		int columnsWidth[] = { 115, 412 - 115 };
		contentStream.addRect(100 + ((pageWidth - 200) / 2) - 100 / 2, pageHeight - (150 / 2) + 10, 75, -(150 / 2));
		contentStream.setStrokingColor(Color.red);
		contentStream.setLineWidth(1);
		contentStream.stroke();

		initX = 100;
		initY = pageHeight - 150;
		int noOfRowsPart1 = this.part1.size();
		updatedTotalHeight = this.createTable(contentStream, initX, initY, part1ColumnWidth, noOfRowsPart1, this.part1, null, false);

		initX = 100;
		initY = (initY - updatedTotalHeight) - 5;

	
		contentStream.setStrokingColor(Color.gray);
		contentStream.setLineWidth(2);
		contentStream.moveTo(initX, initY);
		contentStream.lineTo(
				initX + part1ColumnWidth.stream()
                .mapToInt(columnInfo::getWidth)
                .sum(), 
                initY);
		contentStream.stroke();

		initX = 100;
		initY = initY - 15;

		this.createSingleLineText(contentStream, "PART - A", 10, initX + 5, initY, PDType1Font.HELVETICA_BOLD,
				fontSize);

		initX = 100;
		initY = initY - 10;

		int noOfRowsPart2A = this.part2A.size();
		updatedTotalHeight = this.createTable(contentStream, initX, initY, part2AColumnWidth, noOfRowsPart2A, this.part2A, null, false);

		initX = 100;
		initY = (initY - updatedTotalHeight) - 5;

		contentStream.setStrokingColor(Color.gray);
		contentStream.setLineWidth(2);
		contentStream.moveTo(initX, initY);
		contentStream.lineTo(
				initX + part2AColumnWidth.stream()
                .mapToInt(columnInfo::getWidth)
                .sum(), 
                initY);
		contentStream.stroke();

		initX = 100;
		initY = initY - 15;

		this.createSingleLineText(contentStream, "PART - B", 10, initX + 5, initY, PDType1Font.HELVETICA_BOLD,
				fontSize);

		initX = 100;
		initY = initY - 10;

		// total table width  = 527 ,  but as we are giving padding of 10, we need remove it form main width = 507
		int columnsWidth2[] = { 30, 50,40,76,76,65,65 };
		int noOfRowsPart3B = this.part3B.size();
		updatedTotalHeight = this.createTable(contentStream, initX+5, initY, part3BColumnWidth, noOfRowsPart3B+1, null, this.part3B, true);

		initX = 100;
		initY = (initY - updatedTotalHeight) - 20;
		
		contentStream.addRect(initX + ((pageWidth - 200) / 2) - 100 / 2, initY, 60, -(30));
		contentStream.setStrokingColor(Color.red);
		contentStream.setLineWidth(1);
		contentStream.stroke();
		
		initX = 100;
		initY = (initY - 30) - 6;
		
		this.createSingleLineText(contentStream, "201304541660", 5, (initX + ((pageWidth - 200) / 2) - 100 / 2) + 10, initY, PDType1Font.HELVETICA, 6f);
		
		initX = 100;
		initY = initY - 10;
		
		contentStream.setStrokingColor(Color.gray);
		contentStream.setLineWidth(2);
		contentStream.moveTo(initX, initY);
		contentStream.lineTo(
				initX + part2AColumnWidth.stream()
                .mapToInt(columnInfo::getWidth)
                .sum(), 
                initY);
		contentStream.stroke();
		
		contentStream.close();
		document.save(new File("src\\\\main\\\\resources\\\\document\\\\EwayBill1.pdf"));
		document.close();

	}

	public float createTable(PDPageContentStream contentStreamValue, float InitX, float InitY, List<columnInfo> columnsWidth,
			int noOfRows, List<rowInfo> partNo, List<Map<String, String>> partList, boolean isBOrder) throws IOException {

		float mainInitX = InitX;
		float mainInitY = InitY;
		float totalWidth = 0;
		float totalHeight = 0;

		float cellHeight = 15;
		String textData[] = { "How are you",
				"Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello" };
		String text = "";
//		Set<String> myTextData = partNo.keySet();
//		List<String> textDataForKeyValueTable = partNo != null ? new ArrayList<String>(partNo.keySet()) : null; // if we get key value pair table 
		List<String> textDataForProperTable = partList != null ? new ArrayList<String>(partList.get(0).keySet()) : null; // if we want excel table

		int textDataCounter = 0;

		PDFont font = PDType1Font.HELVETICA;
		float fontSize = 8;

		for (int i = 0; i < noOfRows; i++) {

			for (int j = 0; j < columnsWidth.size(); j++) {
				if (partNo != null) {
					if (j == 0) {
						
//						text =   partNo.stream()
//				                .filter(rowInfo -> rowInfo.getSeq() == i)
//				                .map(rowInfo::getKey)
//				                .findFirst()
//				                .orElse(null);
						
						text = (String) partNo.get(i).getKey();
						font = PDType1Font.HELVETICA;
						fontSize = 7.5f;
					} else if (j != 0) {
						text = (String) partNo.get(i).getValue();
						font = PDType1Font.HELVETICA_BOLD;
						fontSize = 8f;
					}
				}
				
				if(partList != null && i == 0) {
					
					
					for(columnInfo item12 : columnsWidth) {
						if(item12.getSeq() == j) {
							text = (String) item12.getColName();
						}
					}
					
//					text = (String) textDataForProperTable.get(j);
					font = PDType1Font.HELVETICA_BOLD;
					fontSize =  7f;
				}
				else if(partList != null && i != 0) {
					
					for(columnInfo item12 : columnsWidth) {
						if(item12.getSeq() == j) {
							text = partList.get(i-1).get(item12.getColName());
						}
					}
					
//					text = partList.get(i-1).get(textDataForProperTable.get(j));
					font = PDType1Font.HELVETICA;
					fontSize = 7f;
				}

				float fontWidth = font.getStringWidth(text) / 1000 * fontSize;

				int textMinLength = (int) (text.length() / (fontWidth / (columnsWidth.get(j).getWidth() - 10)));
				int startTextPosition = 0;
				int leadingTrack = 0;

				if (fontWidth > columnsWidth.get(j).getWidth() - 20) {
					leadingTrack = 0;
					contentStreamValue.beginText();
					contentStreamValue.setLeading(10); // as a font size
					contentStreamValue.newLineAtOffset(InitX + 5, (InitY - 10));
					contentStreamValue.setFont(font, fontSize);

					for (int k = 0; k < fontWidth / (columnsWidth.get(j).getWidth() - 10); k++) {

						contentStreamValue.showText(startTextPosition + textMinLength < text.length()
								? text.substring(startTextPosition, startTextPosition + textMinLength)
								: text.substring(startTextPosition,
										startTextPosition + (text.length() - startTextPosition)));

						if (k + 1 < fontWidth / (columnsWidth.get(j).getWidth() - 10)) {
							contentStreamValue.newLine();
						}
						leadingTrack += 10;

						if (leadingTrack >= cellHeight) {
							cellHeight += 10; // as a font size
						}

						startTextPosition += textMinLength;
					}
					contentStreamValue.endText();
				} else {
					contentStreamValue.beginText();
					contentStreamValue.newLineAtOffset(InitX + 5, (InitY - 10));
					contentStreamValue.setFont(font, fontSize);
					contentStreamValue.showText(text);
					contentStreamValue.endText();
				}

				InitX += columnsWidth.get(j).getWidth();

				if (j + 1 == columnsWidth.size()) {
					InitX = mainInitX;
					cellHeight += 5;
					for (int l = 0; l < columnsWidth.size(); l++) {
						contentStreamValue.addRect(InitX, InitY, columnsWidth.get(l).getWidth(), -cellHeight);
						InitX += columnsWidth.get(l).getWidth();

						//draw broder for header
						if(i==0 && isBOrder) {
							contentStreamValue.setStrokingColor(Color.DARK_GRAY);
						}
						else {
							contentStreamValue.setStrokingColor(Color.red);
						}
						contentStreamValue.setLineWidth(0.5f);
						contentStreamValue.stroke();
					}
				}
				textDataCounter++;
//				contentStreamValue.addRect(InitX, InitY, columnsWidth[j], -cellHeight);
//				InitX += columnsWidth[j];
//
//				contentStreamValue.setStrokingColor(Color.red);
//				contentStreamValue.setLineWidth(0.5f);
//				contentStreamValue.stroke();

			}
		
		
			
//			vertical column border -- should be done at the end or completion of table
			if(i+1 == noOfRows && isBOrder) {
				InitX = mainInitX;
				InitY = mainInitY;
				totalHeight += cellHeight;
				for (int m = 0; m < columnsWidth.size(); m++) {
					contentStreamValue.addRect(InitX, InitY, columnsWidth.get(m).getWidth(), -totalHeight);
					InitX += columnsWidth.get(m).getWidth();

					contentStreamValue.setStrokingColor(Color.DARK_GRAY);
					contentStreamValue.setLineWidth(0.5f);
					contentStreamValue.stroke();
				}
			}else {
				InitX = mainInitX;
				InitY -= cellHeight;
				totalHeight += cellHeight;
			}
			
//			Horizontal Row border
//			if(isBOrder) {
//				InitX = mainInitX;
//				InitY = mainInitY;
//				totalHeight += cellHeight;
//				for (int m = 0; m < columnsWidth.size(); m++) {
//					contentStreamValue.addRect(InitX, InitY, columnsWidth.get(m).getWidth(), -totalHeight);
//					InitX += columnsWidth.get(m).getWidth();
//
//					contentStreamValue.setStrokingColor(Color.DARK_GRAY);
//					contentStreamValue.setLineWidth(0.5f);
//					contentStreamValue.stroke();
//				}
//			}else {
//				InitX = mainInitX;
//				InitY -= cellHeight;
//				totalHeight += cellHeight;
//			}
			
//			InitX = mainInitX;
//			InitY -= cellHeight;
//			totalHeight += cellHeight;
			
			cellHeight = 15; // initialize with starting value
		}

		return totalHeight;
	}

	public void createSingleLineText(PDPageContentStream contentStream, String text, float leading, float initX,
			float initY, PDFont font, float fontSize) throws IOException {
		contentStream.beginText();
		contentStream.setLeading(leading);
		contentStream.newLineAtOffset(initX, initY);
		contentStream.setFont(font, fontSize);
		contentStream.showText(text);
		contentStream.endText();
	}

	public void initializeData() {

		// 1st Section
//		this.part1 = new HashMap<>() {
//			{
//				put("E-Way Bill No", "2013 0454 1660");
//				put("E-Way Bill Date", "07/06/2021 01:12 PM");
//				put("Generated By", "27AAD CC664 5D1ZS - CORNING TECHNOLOGIES INDIA PRIVATE LIMITED");
//				put("Valid From", "07/06/2021 01:12 PM [723Kms]");
//				put("Valid Until", "11/06/2021");
//				put("IRN", "21e93e964919870e64947dd3206873954c804ca580501cc4dc3d8dc8f350f93b");
//			}
//		};

		
		this.part1 = new ArrayList<rowInfo>();
		this.part1.add(new rowInfo("E-Way Bill No", "2013 0454 1660", 0));
		this.part1.add(new rowInfo("E-Way Bill Date", "07/06/2021 01:12 PM", 1));
		this.part1.add(new rowInfo("Generated By", "27AAD CC664 5D1ZS - CORNING TECHNOLOGIES INDIA PRIVATE LIMITED", 2));
		this.part1.add(new rowInfo("Valid From", "07/06/2021 01:12 PM [723Kms]", 3));
		this.part1.add(new rowInfo("Valid Until", "11/06/2021", 4));
		this.part1.add(new rowInfo("IRN", "21e93e964919870e64947dd3206873954c804ca580501cc4dc3d8dc8f350f93b", 5));
		
		this.part1ColumnWidth = new ArrayList<columnInfo>();
		this.part1ColumnWidth.add(new columnInfo(1,115,"first"));
		this.part1ColumnWidth.add(new columnInfo(2, 412 - 115,"second"));
		
		// 2nd Section Part A
//		this.part2A = new HashMap<>() {
//			{
//				put("GSTIN of Supplier", "27AADCC6645D1ZS,Corning Technologies India Pvt.Ltd");
//				put("Place of Dispatch", "Thane,MAHARASHTRA-421302");
//				put("GSTIN of Recipient", "36AAB CB382 2B1ZB ,BHARAT BIOTECH INTERNATIONAL LTD#15046002");
//				put("Place of Delivery", "HYDERABAD,TELANGANA-500078");
//				put("Document No", "IN2090I81742");
//				put("Document Date", "07/06/2021");
//				put("Transaction Type", "Combination of 2 and 3");
//				put("Value of Goods", "3747421.12");
//				put("HSN Code", "39269099 - CELLSTACK40-STACK2 SOLID CAP#7059");
//				put("Reason for Transportation", "Outward - Supply");
//				put("Transporter", "07AEKPG5291D1ZK & AGARWAL TRANSPORT ORGANISATION");
//			}
//		};

		this.part2A = new ArrayList<rowInfo>();
		this.part2A.add(new rowInfo("GSTIN of Supplier", "27AADCC6645D1ZS,Corning Technologies India Pvt.Ltd", 0));
		this.part2A.add(new rowInfo("Place of Dispatch", "Thane,MAHARASHTRA-421302", 1));
		this.part2A.add(new rowInfo("GSTIN of Recipient", "36AAB CB382 2B1ZB ,BHARAT BIOTECH INTERNATIONAL LTD#15046002", 2));
		this.part2A.add(new rowInfo("Place of Delivery", "HYDERABAD,TELANGANA-500078", 3));
		this.part2A.add(new rowInfo("Document No", "IN2090I81742", 4));
		this.part2A.add(new rowInfo("Document Date", "07/06/2021", 5));
		this.part2A.add(new rowInfo("Transaction Type", "Combination of 2 and 3", 6));
		this.part2A.add(new rowInfo("Value of Goods", "3747421.12", 7));
		this.part2A.add(new rowInfo("HSN Code", "39269099 - CELLSTACK40-STACK2 SOLID CAP#7059", 8));
		this.part2A.add(new rowInfo("Reason for Transportation", "Outward - Supply", 9));
		this.part2A.add(new rowInfo("Transporter", "07AEKPG5291D1ZK & AGARWAL TRANSPORT ORGANISATION", 10));
		
		this.part2AColumnWidth = new ArrayList<columnInfo>();
		this.part2AColumnWidth.add(new columnInfo(1,115,"first"));
		this.part2AColumnWidth.add(new columnInfo(2, 412 - 115,"second"));
		
		
		
		// 3rd Section Part B
		Map<String, String> map1 = new HashMap<>() {
			{
				put("Mode", "Road");
				put("Vehicle / Trans Doc No & Dt.", "MH46BM3388");
				put("From", "Thane");
				put("Entered Date", "07/06/2021 01:12 PM");
				put("Entered By", "27AADCC6645D1ZS");
				put("CEWB No. (If any)", "testNum12345");
				put("Multi Veh.Info (If any)", "testNum12345");
			}
		};
		Map<String, String> map2 = new HashMap<>() {
			{
				put("Mode", "Road");
				put("Vehicle / Trans Doc No & Dt.", "MH46BM3388");
				put("From", "Thane");
				put("Entered Date", "07/06/2021 01:12 PM");
				put("Entered By", "27AADCC6645D1ZS");
				put("CEWB No. (If any)", "testNum12345");
				put("Multi Veh.Info (If any)", "testNum12345");
			}
		};
		
		this.part3B = new ArrayList<Map<String, String>>();
		this.part3B.add(map1);
		this.part3B.add(map2);

		int columnsWidth2[] = { 30, 50,40,76,76,65,65 };
		this.part3BColumnWidth = new ArrayList<columnInfo>();
		this.part3BColumnWidth.add(new columnInfo(0,30,"Mode"));
		this.part3BColumnWidth.add(new columnInfo(1,50,"Vehicle / Trans Doc No & Dt."));
		this.part3BColumnWidth.add(new columnInfo(2,40,"From"));
		this.part3BColumnWidth.add(new columnInfo(3,76,"Entered Date"));
		this.part3BColumnWidth.add(new columnInfo(4,76,"Entered By"));
		this.part3BColumnWidth.add(new columnInfo(5,65,"CEWB No. (If any)"));
		this.part3BColumnWidth.add(new columnInfo(6,65,"Multi Veh.Info (If any)"));

	}

}
