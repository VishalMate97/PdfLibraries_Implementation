package com.service;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Font;

@Service
public class EInvoicePDFImplementationServiceImpl implements EInvoicePDFImplementationService{

	public void createPageWithSize() throws IOException {
		PDDocument document = new PDDocument();
		PDRectangle pageSize = new PDRectangle(840, 600);
		PDPage page = new PDPage(pageSize);
		document.addPage(page);
		
		PDPageContentStream contentStream = new PDPageContentStream(document,page);
		contentStream.beginText();
		contentStream.newLineAtOffset(50, page.getTrimBox().getHeight() - 50);
		contentStream.setLeading(14);
		contentStream.setFont(PDType1Font.TIMES_BOLD, 14);
		contentStream.showText("Working For Invoice");
		contentStream.endText();
		contentStream.close();
		
		document.save(new File("src\\main\\resources\\document\\EinvoiceNew1.pdf"));
		document.close();
		
	}
	
}
