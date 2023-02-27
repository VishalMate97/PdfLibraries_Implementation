package com.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import java.awt.Color;
import java.io.IOException;


public class pdfBoxWIthExistingFlowServiceImpl {

	public void insetts() throws IOException {
		PDDocument document = new PDDocument();
		PDPage page = new PDPage();
		document.addPage(page);
		PDPageContentStream contentStream = new PDPageContentStream(document, page);

		// Define the rectangle coordinates
		float x = 100;
		float y = 500;
		float width = 200;
		float height = 100;

		// Define the border width and colors
		float borderWidth = 2;
		Color borderColor = Color.RED;
		Color fillColor = Color.WHITE;

		// Draw the rectangle with custom borders
//		contentStream.addRect(x, y, width, height);
//		contentStream.setNonStrokingColor(fillColor);
//		contentStream.fill();

		// Set the border colors for each side of the rectangle
		
		// left side
		contentStream.setStrokingColor(borderColor);
		contentStream.moveTo(x, y);
		contentStream.lineTo(x, y+height);
		contentStream.closePath();
		contentStream.stroke();
		
		// upper side
		contentStream.setStrokingColor(borderColor);
		contentStream.moveTo(x, y + height);
		contentStream.lineTo(x+width, y + height);
		contentStream.closePath();
		contentStream.stroke();
		
		// right side
		contentStream.setStrokingColor(borderColor);
		contentStream.moveTo(x+width, y + height);
		contentStream.lineTo(x+width, y);
		contentStream.closePath();
		contentStream.stroke();
		
		// bottom side
		contentStream.setStrokingColor(borderColor);
		contentStream.moveTo(x+width, y);
		contentStream.lineTo(x, y);
		contentStream.closePath();
		contentStream.stroke();
		
		contentStream.close();
		document.save("src\\\\main\\\\resources\\\\document\\\\custom_rectangle.pdf");
		document.close();

	}

	
	
}
