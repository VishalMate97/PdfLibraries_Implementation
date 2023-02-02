package com.controller;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.model.requestParam;

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
	
	@GetMapping("/editExistingPdf")
	@ResponseBody
	public String editExistingPdfAPI(@RequestBody requestParam param) {
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
}
