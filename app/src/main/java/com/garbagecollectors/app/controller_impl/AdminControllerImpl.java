package com.garbagecollectors.app.controller_impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.garbagecollectors.app.dto.EventDto;
import com.garbagecollectors.app.dto.EventsResponse;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.dto.VerifyEventRequest;
import com.garbagecollectors.app.model.Event;
import com.garbagecollectors.app.model.User;
import com.garbagecollectors.app.model.enums.ERole;
import com.garbagecollectors.app.service.EventService;
import com.garbagecollectors.app.service.UserService;

@Service
public class AdminControllerImpl {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private HttpServletRequest hsr;
	
	@Autowired
	private UserService userService;
	
	
    public EventsResponse getFinishedAndNotVerifiedEvents() {
    	
    	EventsResponse response = new EventsResponse();
    	
    	List<EventDto> listEventDto = new ArrayList<EventDto>();
    	
    	Set<Event> events = eventService.findByFinishedAndVerified(true, false);
    	
    	if (!events.isEmpty()) events.stream().forEach(event -> {
    		
    		EventDto dto = new EventDto(); 
    		
    		dto.setEventId(event.getEvent_id());
    		dto.setImageURLend(event.getEnd_picture().getPicture_url());
    		dto.setEventName(event.getEvent_name());
    		dto.setOrganizedBy(event.getIsOrganizedBy().getUserProfile().getFirst_name() + " " + event.getIsOrganizedBy().getUserProfile().getLast_name());
    		dto.setEventDescription(event.getEvent_desc());
    		dto.setSuccessfull(event.isSuccessfull());
    		
    		
    		listEventDto.add(dto);
    		
    		
    	});
    	
    	response.setEvents(listEventDto);
        response.setStringResponse(new StringResponse(200, false, messageSource.getMessage("finished.non.verified", null, new Locale("en"))));

    	
    	return response;
    	
    }
    
    public StringResponse verifyEvent(VerifyEventRequest request) {
    	
    	 String jwtToken = hsr.getHeader("Authorization").substring(7);
         User user = userService.findByJwt(jwtToken);
         
         
		 if (user != null ) {
			 
			 ERole role = user.getUser_role();
			 
			 
			 if (role == ERole.ADMIN) {
				 
				//automatically sets verify to 'true'
				 eventService.verifyAndSetIsSuccessful(request.isSuccessfull(), request.getEventId()); 
				 
				 //update points to user
				if (request.getCredit() > 0) {
					Event event = eventService.findEventById(request.getEventId());
					
					User u = event.getIsOrganizedBy();
					
					u.setCredit(u.getCredit()+request.getCredit());
					
					userService.save(u);
					
				}
				
				 return new StringResponse(200, false, messageSource.getMessage("updated.event", null, new Locale("en")));
				 
			 } else {
				 return new StringResponse(403, false, messageSource.getMessage("authorization.error", null, new Locale("en")));
			 }
			 
			 
        	 
         } else {
        	 return new StringResponse(401, false, messageSource.getMessage("authentication.error", null, new Locale("en")));
         }
    	
    }
	
	

}
