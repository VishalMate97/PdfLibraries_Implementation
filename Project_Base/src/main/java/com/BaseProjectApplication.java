package com;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.service.EInvoicePDFImplementationService;
import com.service.EInvoicePDFImplementationServiceImpl;
import com.service.pdfBoxWIthExistingFlowServiceImpl;
import com.service.tempExamples;
import com.temp.TempLogic;

@SpringBootApplication
public class BaseProjectApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(BaseProjectApplication.class, args);
		
		TempLogic temp = new TempLogic();
		pdfBoxWIthExistingFlowServiceImpl temp1 = new pdfBoxWIthExistingFlowServiceImpl();
		EInvoicePDFImplementationService temp2 = new EInvoicePDFImplementationServiceImpl();
		tempExamples temp3 = new tempExamples();
		
		temp.createNewPdf();
//		temp1.insetts1();
//		temp1.insetts2();
//		temp1.createExistingLogic();
//		temp2.createPageWithSize();
//		temp3.draw1();
	}
	
	
}
