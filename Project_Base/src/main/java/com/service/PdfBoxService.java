package com.service;

import java.io.IOException;

import org.springframework.web.bind.annotation.RequestBody;

import com.model.requestParam;

public interface PdfBoxService {
	public String createAndAddPageInPdfAPI(  requestParam params);
	public String loadExistingPdfAPI(  requestParam param);
	public String editExistingPdfAPI(  requestParam param);
	public String editExistingPdfAddMultipleLinesAPI(  requestParam param);
	public String removeExistingPageAPI(  requestParam param);
	public String addRectangleInPdfPageAPI(  requestParam param) throws IOException;
	public String addTableInPdfPageAPI(  requestParam param) throws IOException;
	public String checkingComplexityAPI(  requestParam param) throws IOException;
	
}
