package com.garbagecollectors.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.garbagecollectors.app.controller_impl.AdminControllerImpl;
import com.garbagecollectors.app.dto.EventsResponse;

@RestController
@RequestMapping("/garbagecollectors")
public class AdminController {
	
		@Autowired
		private AdminControllerImpl adminControllerImpl;
	
		//eventi koje je user zavrsio a admin nije verifikovao
	 	@GetMapping(value = "events/finished/unverified") //pristup: admin
	    @ResponseBody
	    public EventsResponse getFinishedAndNotVerifiedEvents() {
	    	
	    	EventsResponse response = adminControllerImpl.getFinishedAndNotVerifiedEvents();
	    	
	    	return response;
	    	
	    }
	

}
