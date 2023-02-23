package com.temp;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import com.itextpdf.text.Document;
import com.utility.MainTable;

public class TempLogic {

	public void createNewPdf() throws IOException {

		PDDocument document = new PDDocument();
		PDRectangle pageSize = new PDRectangle(400, 400);
		PDPage page = new PDPage(pageSize);
		document.addPage(page);
		
		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		
		float pageHeight = page.getTrimBox().getHeight();
		float pageWidth = page.getTrimBox().getWidth();
		
		float mainPageHeight = pageHeight-40;
		float mainPageWidth = pageWidth-40;
		float mainPageInitX = 20;
		float mainPageInitY = pageHeight - 20;
		
//		contentStream.addRect(mainPageInitX, mainPageInitY, mainPageWidth, -mainPageHeight); 
//		contentStream.setStrokingColor(Color.DARK_GRAY);
//		contentStream.setLineWidth(0.5f);
//		contentStream.stroke();
	
////		  com.utility.MainTable.MainTable(float initX, float initY, float tblWidth, float tblHeight, float noOfCols, float noOfRows, PDPageContentStream contentStream)
		MainTable myTable = new MainTable(mainPageInitX, mainPageInitY, mainPageWidth, mainPageHeight, 4f, 7f, contentStream);
		
		myTable.drawTable();

		
		
		
		contentStream.close();
		
		document.save(new File("src\\\\main\\\\resources\\\\document\\\\tempLogic.pdf"));
		document.close();
		
		
	}

	
}
