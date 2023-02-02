package com.controller;

import java.io.File;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PdfBoxController {

	@GetMapping("/savePdf")
	@ResponseBody
	public String firstPdfBoxAPI(@RequestParam String path1) {

//		String path = "src"+File.separator+"main"+File.separator+"resources"+File.separator+"document"+File.separator+"myfile.pdf";
		String path = "src/main/resources/document/myfile.pdf";//create new File If doesn't exist or it will replace If exists
//		String path = "D:\\Temp\\Documents\\myfile.pdf";
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
}
