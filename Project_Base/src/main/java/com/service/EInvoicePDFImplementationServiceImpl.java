package com.service;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Font;
import com.model.textFontData;

@Service
public class EInvoicePDFImplementationServiceImpl implements EInvoicePDFImplementationService{

	public void createPageWithSize() throws IOException {
		PDDocument document = new PDDocument();
		PDRectangle pageSize = new PDRectangle(840, 600);
		PDPage page = new PDPage(pageSize);
		document.addPage(page);
		
		PDFont Segoe_UI_Semilight = PDType0Font.load(document, new File("src\\main\\resources\\font\\segoeuisl.ttf"));
		PDFont Times_Roman_Bold = PDType0Font.load(document, new File("src\\main\\resources\\font\\Time_Roman_Bold.ttf"));
		PDFont Times_Roman_Normal = PDType0Font.load(document, new File("src\\main\\resources\\font\\Time_Roman_Normal.ttf"));
				
		PDImageXObject imageObject = PDImageXObject.createFromFile("src\\main\\resources\\img\\logo.png", document);
		
		float pageHeight = page.getTrimBox().getHeight();
		float pageWidth = page.getTrimBox().getWidth();
		
		PDPageContentStream contentStream = new PDPageContentStream(document,page);
		
		// mainPage Cordinates, height and width
		float mainPageHeight = pageHeight-70;
		float mainPageWidth = pageWidth-70;
		float mainPageInitX = 35;
		float mainPageInitY = pageHeight - 35;
		
		// Main page rectangle border
		contentStream.setStrokingColor(Color.DARK_GRAY);
		contentStream.setLineWidth(0.5f);
		createRectangles(contentStream, mainPageInitX, mainPageInitY , mainPageWidth, -mainPageHeight);
		//Now Draw Inner Rectangles
		createRectangles(contentStream, mainPageInitX, mainPageInitY , mainPageWidth/3, -110);
		contentStream.drawImage(imageObject, mainPageInitX, mainPageInitY-31, mainPageWidth/3, 31);
		
//		String text1 = "SUPPLIER";
//		String text2 = "ASB International";
//		String text3 = "Ambernath, Ambernath-421501";
//		String text4 = "GSTIN : 27AAACA8424F1ZW";
//		String text5 = "PAN : ";
//		String text6 = "PANValue";
//		String text7 = "CIN : ";
//		String text8 = "CINValue";
		
		
		List<textFontData> listOftextFontData = new ArrayList<textFontData>();
		listOftextFontData.add(new textFontData("SUPPLIER",Times_Roman_Bold,9f,14f));
		listOftextFontData.add(new textFontData("ASB International",Times_Roman_Bold,7f,14f));
		listOftextFontData.add(new textFontData("Ambernath, Ambernath-421501",Times_Roman_Normal,7f,9f));
		listOftextFontData.add(new textFontData("GSTIN : 27AAACA8424F1ZW",Times_Roman_Normal,7f,9f));
		listOftextFontData.add(new textFontData("PAN : ",Times_Roman_Normal,7f,0));
		listOftextFontData.add(new textFontData("PANValue",Times_Roman_Normal,7f,0));
		listOftextFontData.add(new textFontData("CIN : ",Times_Roman_Normal,7f,0));
		listOftextFontData.add(new textFontData("CINValue",Times_Roman_Normal,7f,0));

		createMultiLinesTextWithNewLine(contentStream, listOftextFontData.subList(0, 4), mainPageInitX+5, (mainPageInitY-31)- 15);
		createMultiLinesTextWithoutNewLine(contentStream, listOftextFontData.subList(4, 6), mainPageInitX+5, (mainPageInitY-110)+ 5);
		createMultiLinesTextWithoutNewLine(contentStream, listOftextFontData.subList(6, 8), (mainPageInitX + (mainPageWidth/3)/2) + 5, (mainPageInitY-110)+ 5);

		
		//we were drawing first Section by this
//		contentStream.beginText();
//		contentStream.setLeading(14f);
//		contentStream.newLineAtOffset(mainPageInitX+5, (mainPageInitY-31)- 15);
//		
//		contentStream.setFont(Times_Roman_Bold, 9f);
//		contentStream.showText(text1);
//		contentStream.newLine();
//		contentStream.setFont(Times_Roman_Bold, 7f);
//		contentStream.showText(text2);
//		contentStream.newLine();
//		
//		contentStream.setLeading(9f);
//		contentStream.setFont(Times_Roman_Normal, 7f);
//		contentStream.showText(text3);
//		contentStream.newLine();
//		contentStream.showText(text4);
//		contentStream.endText();
//		
//		contentStream.beginText();
//		contentStream.newLineAtOffset(mainPageInitX+5, (mainPageInitY-110)+ 5);	
//		contentStream.setFont(Times_Roman_Normal, 7f);
//		contentStream.showText(text5);
//		contentStream.showText(text6);
//		contentStream.endText();
//		
//		contentStream.beginText();
//		contentStream.newLineAtOffset(  (mainPageInitX + (mainPageWidth/3)/2) + 5, (mainPageInitY-110)+ 5);	
//		contentStream.setFont(Times_Roman_Normal, 7f);
//		contentStream.showText(text7);
//		contentStream.showText(text8);
//		contentStream.endText();
		
		
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3), mainPageInitY , mainPageWidth/3 + mainPageWidth/3, -30);
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3), mainPageInitY-30 , mainPageWidth/3, - (110 - 30));
		
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30 , mainPageWidth/3, - (16));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-16 , mainPageWidth/3, - (16));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-32 , mainPageWidth/3, - (16));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-48 , mainPageWidth/3, - (16));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-64 , mainPageWidth/3, - (16));
		
		createRectangles(contentStream, mainPageInitX, mainPageInitY-110 , mainPageWidth/3, - (110 - 30));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3), mainPageInitY-110 , mainPageWidth/3, - (110 - 30));
		
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-80 , mainPageWidth/3, - (16));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-96 , mainPageWidth/3, - (16));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-112 , mainPageWidth/3, - (16));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-128 , mainPageWidth/3, - (16));
		createRectangles(contentStream, mainPageInitX+(mainPageWidth/3)+(mainPageWidth/3), mainPageInitY-30-144 , mainPageWidth/3, - (16));
		contentStream.stroke();
		
		float columnsWidth[] = {40,250,80,40,40,40,40,80,50,110};
		
		// now draw Dynamic table structure
		float tableHeight = createTable(contentStream, mainPageInitX , mainPageInitY - (110 +(110-30) + 15), columnsWidth , 15);
		float updatedInitX = mainPageInitX;
		float updatedInitY = mainPageInitY;
		
		for(int i=0; i< columnsWidth.length; i++) {
			createRectangles(contentStream, updatedInitX, mainPageInitY - (110 +(110-30)) , columnsWidth[i], -15); // header
			createRectangles(contentStream, updatedInitX, mainPageInitY - (110 +(110-30) + 15) , columnsWidth[i], -mainPageHeight + (110 +(110-30) + 15)); // body
			createRectangles(contentStream, updatedInitX, mainPageInitX , columnsWidth[i], 15); // footer
			updatedInitX += columnsWidth[i];
		}
		contentStream.setStrokingColor(Color.green);
		contentStream.setLineWidth(0.5f);
		contentStream.stroke();
		
		contentStream.close();
		
		document.save(new File("src\\main\\resources\\document\\EinvoiceNew1.pdf"));
		document.close();
		
	}
	
	public void createRectangles(PDPageContentStream contentStreamValue, float InitX, float InitY, float width, float height) throws IOException {
		contentStreamValue.addRect(InitX, InitY, width, height);
	}
	
	public float createTable(PDPageContentStream contentStreamValue, float InitX, float InitY, float[] columnsWidth, int noOfRows) throws IOException {
		
		float mainInitX = InitX;
		float mainInitY = InitY;
		float totalWidth = 0;
		float totalHeight = 0;
		
		for(int i = 0 ; i< noOfRows; i++) {
			
			for(int j = 0 ; j< columnsWidth.length; j++) {
				contentStreamValue.addRect(InitX, InitY, columnsWidth[j], -15);
				InitX += columnsWidth[j];
				
				contentStreamValue.setStrokingColor(Color.red);
				contentStreamValue.setLineWidth(0.5f);
				contentStreamValue.stroke();
//				if(i == 0) {
//					contentStreamValue.setStrokingColor(Color.DARK_GRAY);
//					contentStreamValue.setLineWidth(0.5f);
//					contentStreamValue.stroke();
//					totalWidth += columnsWidth[j];
//				}
//				else if(i == noOfRows - 1) {
//					contentStreamValue.setStrokingColor(Color.DARK_GRAY);
//					contentStreamValue.setLineWidth(0.5f);
//					contentStreamValue.stroke();
//				}
//				else {
//					contentStreamValue.setStrokingColor(1f);
//					contentStreamValue.setLineWidth(0.5f);
//					contentStreamValue.stroke();
//				}
			
			}
			
			InitX = mainInitX;
			InitY -= 15;
			totalHeight += 15;
		}
		
//		contentStreamValue.addRect(mainInitX, mainInitY, totalWidth , -15);
//		contentStreamValue.setStrokingColor(Color.DARK_GRAY);
//		contentStreamValue.setLineWidth(0.5f);
//		contentStreamValue.stroke();
		
		return totalHeight;
	}
	
	public void createSingleLineText(PDPageContentStream contentStream, String text, float leading, float initX, float initY, PDFont font, float fontSize) throws IOException {
		contentStream.beginText();
		contentStream.setLeading(leading);
		contentStream.newLineAtOffset(initX, initY);
		contentStream.setFont(font, fontSize);
		contentStream.showText(text);
		contentStream.endText();
	}
	
	public void createMultiLinesTextWithNewLine(PDPageContentStream contentStream, List<textFontData> textData, float initX, float initY) throws IOException {
		contentStream.beginText();
		contentStream.newLineAtOffset(initX, initY);
		
		for(int i =0 ; i< textData.size();i++) {
			if(textData.get(i).getLeading() != 0) {
				contentStream.setLeading(textData.get(i).getLeading());
			}
			contentStream.setFont(textData.get(i).getFont(), textData.get(i).getFontSize());
			contentStream.showText(textData.get(i).getText());
			contentStream.newLine();
		}
		contentStream.endText();
	}
	
	public void createMultiLinesTextWithoutNewLine(PDPageContentStream contentStream, List<textFontData> textData, float initX, float initY) throws IOException {
		contentStream.beginText();
//		contentStream.setLeading(leading); // as we are on same line
		contentStream.newLineAtOffset(initX, initY);
		
		for(int i =0 ; i< textData.size();i++) {
			contentStream.setFont(textData.get(i).getFont(), textData.get(i).getFontSize());
			contentStream.showText(textData.get(i).getText());
		}
		contentStream.endText();
	}
}


