package com.garbagecollectors.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/garbagecollectors")
public class AdminController {
	
	//eventi koje je user zavrsio a admin nije verifikovao
	@GetMapping("admin/events/finished/unverified")
	public String zavrsenEventoviAdminNijeVerifkovao() {
		return "sa"; //true false
	}
	

}
