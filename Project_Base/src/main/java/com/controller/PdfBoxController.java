package com.controller;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.model.requestParam;

import ch.qos.logback.core.pattern.parser.Parser;

@RestController
public class PdfBoxController {

	@GetMapping("/addNewPdf")
	@ResponseBody
	public String createAndAddPageInPdfAPI(@RequestBody requestParam params) {
		
//		create new File If doesn't exist or it will replace If exists
//		String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"document"+File.separator+"myfile.pdf";
//		String path = "src/main/resources/document/myfile.pdf";//create new File If doesn't exist or it will replace If exists
//		String path = "D:\\Temp\\Documents\\myfile.pdf";
		String path = params.getPath();
		PDDocument doc = new PDDocument();
		PDPage p1 = new PDPage();
		try {
			doc.addPage(p1);
			doc.save(path);
			doc.close();
		} catch (Exception | Error ex) {

			ex.printStackTrace();
			return "Unable to save PDF at " + path;
		}
		return "Empty pdf is saved at " + path;
	}
	
	@GetMapping("/loadExistingPdf")
	@ResponseBody
	public String loadExistingPdfAPI(@RequestBody requestParam param) {
		PDDocument doc;
		try {
			doc = PDDocument.load(new File(param.getPath1())); // load Existing PDF file through static load method
			doc.addPage(new PDPage()); // make changes into that existing pdf, here we are adding pages 
			doc.save(param.getPath2());
			doc.close();
		} catch (Exception | Error ex) {
			ex.printStackTrace();
			return "Unable to save Edited PDF";
		}
		return "Edited PDF is saved";
	}
	
	@GetMapping("/editExistingPdf")
	@ResponseBody
	public String editExistingPdfAPI(@RequestBody requestParam param) {
		PDDocument doc;
		try {
			doc = PDDocument.load(new File(param.getPath1())); // load Existing PDF file through static load method
			doc.addPage(new PDPage()); // make changes into that existing pdf, here we are adding pages 
			
			PDPage p1 = doc.getPage(1);
			PDPageContentStream contentStream = new PDPageContentStream(doc, p1);
			contentStream.beginText();
			contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);
			contentStream.newLineAtOffset(20, 450);
			String text = "We Are Inside PDF From PDFBOX API editExistingPdf";
			contentStream.showText(text);
			contentStream.endText();
			contentStream.close();
			
			doc.save(param.getPath1());
			doc.close();
		} catch (Exception | Error ex) {
			ex.printStackTrace();
			return "Unable to save Edited PDF";
		}
		return "Edited PDF is saved";
	}
	
	@GetMapping("/editExistingPdfAddMultipleLines")
	@ResponseBody
	public String editExistingPdfAddMultipleLinesAPI(@RequestBody requestParam param) {
		PDDocument doc;
		try {
			doc = PDDocument.load(new File(param.getPath1())); // load Existing PDF file through static load method
//			doc.addPage(new PDPage()); // make changes into that existing pdf, here we are adding pages 
			
			PDPage p1 = doc.getPage(0);
			PDPageContentStream contentStream = new PDPageContentStream(doc, p1);
			contentStream.beginText();
			contentStream.setLeading(19.5f);
			contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);
			contentStream.newLineAtOffset(40, 750);
			String text1 = "We Are Inside PDF From PDFBOX API editExistingPdf";
			String text2 = "We Are Inside PDF From PDFBOX API editExistingPdf";
			String text3 = "We Are Inside PDF From PDFBOX API editExistingPdf";
			
			contentStream.showText(text1);
			contentStream.newLine();
			contentStream.showText(text2);
			contentStream.newLine();
			contentStream.showText(text3);
			
			contentStream.endText();
			contentStream.close();
			
			doc.save(param.getPath1());
			doc.close();
		} catch (Exception | Error ex) {
			ex.printStackTrace();
			return "Unable to save Edited PDF";
		}
		return "Edited PDF is saved";
	}
	
	@GetMapping("/removeExistingPage")
	@ResponseBody
	public String removeExistingPageAPI(@RequestBody requestParam param) {
		PDDocument doc;
		String message = "";
		try {
			doc = PDDocument.load(new File(param.getPath1())); // load Existing PDF file through static load method
			int pageIndex = param.getPageIndex();
			
			// will remove only when that page is exists
			if(doc.getNumberOfPages() >= pageIndex+1) {
				doc.removePage(pageIndex);
				message = "Page Number : " +  Integer.sum(pageIndex, 1) + " is removed ";
			}
			else {
				message = "Unable to remove Page Number : " +   Integer.sum(pageIndex, 1);
			}
			
			doc.save(param.getPath1());
			doc.close();
		} catch (Exception | Error ex) {
			ex.printStackTrace();
			return "Not worked";
		}
		return message;
	}
	


	@GetMapping("/AddRectangleInPdfPage")
	@ResponseBody
	public String addRectangleInPdfPageAPI(@RequestBody requestParam param) throws IOException {
	
		PDDocument doc = new PDDocument();
		PDPage page1 = new PDPage(); 
		doc.addPage(page1);
		
		PDPageContentStream contentStream = new PDPageContentStream(doc, page1);
		

//		contentStream.setLeading(19.5f);
		
		contentStream.setStrokingColor(Color.DARK_GRAY);
//		contentStream.setLineWidth(1);
		contentStream.addRect(50, 50, 150, 150);
		contentStream.stroke();
		
		
		contentStream.setStrokingColor(Color.red);
//		contentStream.setLineWidth(1);
		contentStream.addRect(50+150, 50+150, 150, 150);
		contentStream.stroke();
		
		contentStream.setStrokingColor(Color.blue);
//		contentStream.setLineWidth(1);
		contentStream.addRect(50+150+150, 50+150+150, 150, 150);
		
		contentStream.stroke();
		contentStream.close();
		
		doc.save(new File("src/main/resources/document/myfilenew3.pdf"));
		doc.close();
		
		return "worked";
		
	}
	
	@GetMapping("/AddTableInPdfPage")
	@ResponseBody
	public String addTableInPdfPageAPI(@RequestBody requestParam param) throws IOException {
	
		PDDocument doc = new PDDocument();
		PDPage page1 = new PDPage(); 
		doc.addPage(page1);
		
		
		int pageHieght = (int) page1.getTrimBox().getHeight();
		int pageWidth = (int) page1.getTrimBox().getWidth();
		
		PDPageContentStream contentStream = new PDPageContentStream(doc, page1);
		
		contentStream.setStrokingColor(Color.DARK_GRAY);
		contentStream.setLineWidth(1);
		
		
		int initX = 50;
		int initY = pageHieght-50;
		int cellHieght = 30;
		int cellWidth = 100;
		
		int countCol = 5;
		int coutRow = 7;
		
		for(int i = 0; i<coutRow;i++) {
			for(int j = 0; j< countCol; j++) {
				contentStream.addRect(initX, initY, cellWidth, -cellHieght);
				
				contentStream.beginText();
				contentStream.newLineAtOffset(initX +10, initY - cellHieght + 10);
				contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);
				contentStream.showText("hello");
				contentStream.endText();
				
				
				initX += cellWidth;
			}
			initX = 50;
			initY -=  cellHieght;
		}
		
		contentStream.stroke();
		contentStream.close();
		
		doc.save(new File("src/main/resources/document/myfilenew4.pdf"));
		doc.close();
		
		return "worked";
		
	}
	
	
	
	@GetMapping("/checkingComplexity")
	@ResponseBody
	public String checkingComplexityAPI(@RequestBody requestParam param) throws IOException {
	
		PDDocument doc = new PDDocument();
		PDPage page1 = new PDPage(); 
		doc.addPage(page1);
		
		int h = (int) page1.getTrimBox().getHeight();
		int pageWidth = (int) page1.getTrimBox().getWidth();
		
		PDPageContentStream contentStream = new PDPageContentStream(doc, page1);
		
		contentStream.setStrokingColor(Color.DARK_GRAY);
		contentStream.setLineWidth(1);
		
		int initX = 50;
		int initY = h-50;
		int cellHieght = 30;
		int cellWidth = 100;
		
		int countCol = 5;
		int sssa = 7;
		
		createCellforme(50, h-50, 200, -50, contentStream);
		createCellforme(250, h-50, 200, -100, contentStream);
		createCellforme(450, h-50, 100, -100, contentStream);
		createCellforme(50, h-100, 200, -150, contentStream);
		createCellforme(250, h-150, 300, -50, contentStream);
		createCellforme(50, h-250, 200, -50, contentStream);
		createCellforme(250, h-200, 300, -150, contentStream);
		createCellforme(50, h-300, 200, -150, contentStream);
		createCellforme(250, h-350, 300, -100, contentStream);
		
		contentStream.stroke();
		contentStream.close();
		
		doc.save(new File("src/main/resources/document/myfilenew4.pdf"));
		doc.close();
		
		return "worked";
		
	}
	
	public void createCellforme(int initX, int initY, int cellWidth, int cellHieght, PDPageContentStream contentStream) throws IOException {
		contentStream.addRect(initX, initY, cellWidth, cellHieght);
		
		contentStream.beginText();
		contentStream.newLineAtOffset(initX +10, initY + cellHieght + 10);
		contentStream.setFont(PDType1Font.TIMES_BOLD_ITALIC, 14);
		contentStream.showText("hello");
		contentStream.endText();
	}
}
