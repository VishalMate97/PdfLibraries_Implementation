package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.model.requestParam;
import com.service.PdfBoxService;

@RestController
public class ApiController {

	@Autowired
	PdfBoxService PdfBoxService; // Apache PDFBOX

	@GetMapping("/apachePDFBox")
	@ResponseBody
	public String apachePDFBoxAPI(@RequestBody requestParam params) {

//		RequestBody Example Format
//		{
//		    "path":"src/main/resources/document/PDFBOXfile1.pdf",
//		    "path1":"src/main/resources/document/PDFBOXfile2.pdf",
//		    "path2":"src/main/resources/document/PDFBOXfile3.pdf",
//		    "pageIndex":"1",
//		    "apiNumber":"8"
//		}
//		
		try {
			switch (params.getApiNumber()) {
			case 1:
				return PdfBoxService.createAndAddPageInPdfAPI(params);
			case 2:
				return PdfBoxService.loadExistingPdfAPI(params);
			case 3:
				return PdfBoxService.editExistingPdfAPI(params);
			case 4:
				return PdfBoxService.editExistingPdfAddMultipleLinesAPI(params);
			case 5:
				return PdfBoxService.removeExistingPageAPI(params);
			case 6:
				return PdfBoxService.addRectangleInPdfPageAPI(params);
			case 7:
				return PdfBoxService.addTableInPdfPageAPI(params);
			case 8:
				return PdfBoxService.checkingComplexityAPI(params);
			default:
				return "please add your valid API Number";

			}
		} catch (Exception | Error ex) {
			ex.printStackTrace();
			return "Something Went Wrong in APINumber : " + params.getApiNumber();
		}
	}
	
}
