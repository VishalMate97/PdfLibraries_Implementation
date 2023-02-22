package com;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.temp.TempLogic;

@SpringBootApplication
public class BaseProjectApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(BaseProjectApplication.class, args);
		
		TempLogic temp = new TempLogic();
		
		temp.createNewPdf();
		
	}
	
	
}
