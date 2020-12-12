package com.garbagecollectors.app.controller_impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.garbagecollectors.app.dto.EventDto;
import com.garbagecollectors.app.dto.EventsResponse;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.model.Event;
import com.garbagecollectors.app.service.EventService;

@Service
public class AdminControllerImpl {
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private MessageSource messageSource;
	
	
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
	
	

}
