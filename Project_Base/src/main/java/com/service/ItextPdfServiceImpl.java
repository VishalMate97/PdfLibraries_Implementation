package com.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.model.requestParam;

@Service
public class ItextPdfServiceImpl implements ItextPdfService {

	@Override
	public String createAndAddPageInPdfAPI(requestParam params) {
		// TODO Auto-generated method stub
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
