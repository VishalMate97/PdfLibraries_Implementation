package com.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.model.requestParam;

@Service
public class ItextPdfServiceImpl implements ItextPdfService {

	@Override
	public String createAndAddPageInPdfAPI(requestParam params)  {
		
		try {
			Document document = new Document();
			PdfWriter.getInstance(document, new FileOutputStream("src\\main\\resources\\document\\itextFiles\\itext1.pdf"));
			document.open();
			document.add(new Paragraph("Hello World How Are You"));
			document.close();
		} catch (Exception |Error  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "inside createAndAddPageInPdfAPI";
	}

	@Override
	public String loadExistingPdfAPI(requestParam param) {
		// TODO Auto-generated method stub
		return "inside loadExistingPdfAPI";
	}

	@Override
	public String editExistingPdfAPI(requestParam param) {
		// TODO Auto-generated method stub
		return "inside editExistingPdfAPI";
	}

	@Override
	public String editExistingPdfAddMultipleLinesAPI(requestParam param) {
		// TODO Auto-generated method stub
		return "inside editExistingPdfAddMultipleLinesAPI";
	}

	@Override
	public String removeExistingPageAPI(requestParam param) {
		// TODO Auto-generated method stub
		return "inside removeExistingPageAPI";
	}

	@Override
	public String addRectangleInPdfPageAPI(requestParam param) throws IOException {
		// TODO Auto-generated method stub
		return "inside addRectangleInPdfPageAPI";
	}

	@Override
	public String addTableInPdfPageAPI(requestParam param) throws IOException {
		// TODO Auto-generated method stub
		return "inside addTableInPdfPageAPI";
	}

	@Override
	public String checkingComplexityAPI(requestParam param) throws IOException {
		// TODO Auto-generated method stub
		return "inside checkingComplexityAPI";
	}

}
