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

public class EwayBillServiceImpl implements EwayBillService {
	Map<String, String> part1;
	Map<String, String> part2A;

	public void start() throws IOException {
		
		this.initializeData();
		
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(); // creating page with size(8.5 * 11 inches) --> width = 612, height = 792 
		document.addPage(page);
		
		float pageHeight =  page.getTrimBox().getHeight();
		float pageWidth =  page.getTrimBox().getWidth();
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		
		//text Section
		String text = "Hello All";
		PDFont font = PDType1Font.TIMES_ROMAN;
		float fontSize = 10;
		float x = 50;
		float y =pageHeight-50;
		float leading = 10;
		
//		this.createSingleLineText(contentStream,text,leading,x,y,font,fontSize);
		
		
		//table Section
		float columnsWidth[] = {115,412-115};
//		contentStream.addRect(100, pageHeight-150, pageWidth-200, -(130));
//		contentStream.setStrokingColor(Color.red);
//		contentStream.setLineWidth(1);
//		contentStream.stroke();
		
		int noOfRowsPart1 = 6;
		this.createTable(contentStream,100,pageHeight-150,columnsWidth,noOfRowsPart1);
		
		contentStream.close();
		document.save(new File("src\\\\main\\\\resources\\\\document\\\\EwayBill1.pdf"));
		document.close();
		
		
	}

	
	public float createTable(PDPageContentStream contentStreamValue, float InitX, float InitY, float[] columnsWidth,
			int noOfRows) throws IOException {

		float mainInitX = InitX;
		float mainInitY = InitY;
		float totalWidth = 0;
		float totalHeight = 0;
		
		float cellHeight = 15;
		String textData[] = {"How are you","Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello Hello"};
		String text = "";
//		Set<String> myTextData = this.part1.keySet();
		List<String> myTextData = new ArrayList<String>(this.part1.keySet());
		
		int textDataCounter = 0;
		
		PDFont font = PDType1Font.HELVETICA;
		float fontSize = 8;
		
		for (int i = 0; i < noOfRows; i++) {

			for (int j = 0; j < columnsWidth.length; j++) {
				
				if(j==0) {
					text = (String) myTextData.get(i);
					font = PDType1Font.HELVETICA;
					fontSize = 7.5f;
				}
				else if(j!=0) {
					text = this.part1.get(myTextData.get(i));
					font = PDType1Font.HELVETICA_BOLD;
					fontSize = 8f;
				}
				
				
				
				float fontWidth = font.getStringWidth(text)/1000 * fontSize;
				
				int textMinLength = (int) (text.length()/(fontWidth/(columnsWidth[j]-20)));
				int startTextPosition = 0;
				int leadingTrack = 0;
				
				if(fontWidth > columnsWidth[j]-20) {
					leadingTrack = 0;
					contentStreamValue.beginText();
					contentStreamValue.setLeading(10);  // as a font size
					contentStreamValue.newLineAtOffset(InitX+10, (InitY-10));
					contentStreamValue.setFont(font, fontSize);
					
					for(int k =0; k < fontWidth/(columnsWidth[j]-20); k++) {
						
						contentStreamValue.showText(startTextPosition+textMinLength < text.length() ? 
									text.substring(startTextPosition, startTextPosition+textMinLength):
									text.substring(startTextPosition, startTextPosition+(text.length()-startTextPosition)));
						
						
						if(k+1 < fontWidth/(columnsWidth[j]-20)){
							contentStreamValue.newLine();
						}
						leadingTrack += 10;
						
						if(leadingTrack >= cellHeight) {
							cellHeight += 10; // as a font size
						}
						
						
						startTextPosition += textMinLength;
					}
					contentStreamValue.endText();
				}
				else {
					contentStreamValue.beginText();
					contentStreamValue.newLineAtOffset(InitX+10, (InitY-10));
					contentStreamValue.setFont(font, fontSize);
					contentStreamValue.showText(text);
					contentStreamValue.endText();
				}
				
				
				InitX += columnsWidth[j];
				
				if(j+1 == columnsWidth.length) {
					InitX = mainInitX;
					cellHeight += 5;
					for (int l = 0; l < columnsWidth.length; l++) {
						contentStreamValue.addRect(InitX, InitY, columnsWidth[l], -cellHeight);
						InitX += columnsWidth[l];

						contentStreamValue.setStrokingColor(Color.red);
						contentStreamValue.setLineWidth(0.5f);
						contentStreamValue.stroke();
					}
				}
				textDataCounter ++;
//				contentStreamValue.addRect(InitX, InitY, columnsWidth[j], -cellHeight);
//				InitX += columnsWidth[j];
//
//				contentStreamValue.setStrokingColor(Color.red);
//				contentStreamValue.setLineWidth(0.5f);
//				contentStreamValue.stroke();

			}

			InitX = mainInitX;
			InitY -= cellHeight;
			cellHeight = 15; // initialize with starting value
			totalHeight += cellHeight;
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

		this.part1 = new HashMap<>() {
			{
				put("E-Way Bill No", "2013 0454 1660");
				put("E-Way Bill Date", "07/06/2021 01:12 PM");
				put("Generated By", "27AAD CC664 5D1ZS - CORNING TECHNOLOGIES INDIA PRIVATE LIMITED");
				put("Valid From", "07/06/2021 01:12 PM [723Kms]");
				put("Valid Until", "11/06/2021");
				put("IRN", "21e93e964919870e64947dd3206873954c804ca580501cc4dc3d8dc8f350f93b");
			}
		};

		this.part2A = new HashMap<>() {
			{
				put("GSTIN of Supplier", "27AADCC6645D1ZS,Corning Technologies India Pvt.Ltd");
				put("Place of Dispatch", "Thane,MAHARASHTRA-421302");
				put("GSTIN of Recipient", "36AAB CB382 2B1ZB ,BHARAT BIOTECH INTERNATIONAL LTD#15046002");
				put("Place of Delivery", "HYDERABAD,TELANGANA-500078");
				put("Document No", "IN2090I81742");
				put("Document Date", "07/06/2021");
				put("Transaction Type", "Combination of 2 and 3");
				put("Value of Goods", "3747421.12");
				put("HSN Code", "39269099 - CELLSTACK40-STACK2 SOLID CAP#7059");
				put("Reason for Transportation", "Outward - Supply");
				put("Transporter", "07AEKPG5291D1ZK & AGARWAL TRANSPORT ORGANISATION");
			}
		};

	}

}
