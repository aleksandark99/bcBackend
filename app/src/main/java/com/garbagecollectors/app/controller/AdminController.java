package com.garbagecollectors.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.garbagecollectors.app.controller_impl.AdminControllerImpl;
import com.garbagecollectors.app.dto.EventsResponse;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.dto.VerifyEventRequest;

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
	 	
	 	//napraviti metodu verifyEvent prima id eventa , isSucessfull  (na beku se setuje sucessfull i verifired prelazi u true)
	 	//btw u tu verifyEvent ubacite da se i organizatoru eventa daju poeni!
	 	@PostMapping(value = "event/verify")
	 	@ResponseBody
	 	public StringResponse verifyEvent(@RequestBody VerifyEventRequest request) {
	 		
	 		StringResponse response = adminControllerImpl.verifyEvent(request);
	 		
	 		return response;
	 	}
	 	
	 	
	

}
