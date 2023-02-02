package com.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@GetMapping("/firstAPI")
	@ResponseBody
	public String firstAPI(@RequestParam String param1, @RequestBody Object reqBody) {
		return "hello";
	}
	
}
