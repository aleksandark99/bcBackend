package com.garbagecollectors.app.controller_impl;



import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.garbagecollectors.app.dto.EventDto;
import com.garbagecollectors.app.dto.EventRequest;
import com.garbagecollectors.app.dto.EventResponse;
import com.garbagecollectors.app.dto.EventsResponse;
import com.garbagecollectors.app.dto.ImgBB;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.model.Event;
import com.garbagecollectors.app.model.Picture;
import com.garbagecollectors.app.model.User;
import com.garbagecollectors.app.service.EventService;
import com.garbagecollectors.app.service.PictureService;
import com.garbagecollectors.app.service.UserService;


@Service
public class EventControllerImpl {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private PictureService pictureService;

    @Autowired
    private HttpServletRequest hsr;

    @Autowired
    private MessageSource messageSource;

    private static String UPLOAD_ROOT = "https://api.imgbb.com/1/upload?key=b06a3582d53c4a0be976670478081f5c";

    public Event getEventById(int eventId){

        return eventService.findEventById(eventId);
    }

    public Set<Event> getEventsByUser(int userId){

        return eventService.findEventsByUser(userId);
    }

    public Set<EventResponse> getUnfinishedEvents(){

        Set<EventResponse> findEvents = new HashSet<>();
        Set<Event> events = eventService.findUnfinishedEvents();

       for(Event e : events){

            EventResponse eventResponse = new EventResponse();

            eventResponse.setNameEvent(e.getEvent_name());
            eventResponse.setEventDescription(e.getEvent_desc());
            eventResponse.setImageURL(e.getStart_picture().getPicture_url());
            eventResponse.setLocationString(e.getEvent_location());
            eventResponse.setLocationURL(e.getLocation_url());

            findEvents.add(eventResponse);

       }
       return findEvents;
    }


    public StringResponse createEvent(MultipartFile file, EventRequest event) throws IOException {

        String jwtToken = hsr.getHeader("Authorization").substring(7);
        User user = userService.findByJwt(jwtToken);

        StringResponse response = new StringResponse();

        if(!file.isEmpty()) {

            MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();


            bodyMap.add("image", Base64.getEncoder().encode(file.getBytes()));

            HttpHeaders httpHeaders = new HttpHeaders();

            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String,Object>>(bodyMap, httpHeaders);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ImgBB> responseEntity = restTemplate.exchange(UPLOAD_ROOT, HttpMethod.POST, requestEntity, ImgBB.class);

            String pic_url = responseEntity.getBody().getData().getDisplay_url();

            Picture startPicture = new Picture();
            startPicture.setPicture_url(pic_url);
            startPicture.setPicture_name(file.getOriginalFilename());
            startPicture.setPicture_size(file.getSize());
            startPicture.setMime_type(file.getContentType());
            pictureService.save(startPicture);

            Event newEvent = new Event();
            newEvent.setEvent_name(event.getEventName());
            newEvent.setEvent_desc(event.getEventDescription());
            newEvent.setEvent_location(event.getLocationString());
            newEvent.setLocation_url(event.getLocationURL());

            newEvent.setStart_date(getTodayDateTime());
            newEvent.setIsOrganizedBy(user);
            newEvent.setStart_picture(startPicture);

            eventService.save(newEvent);

            response.setCode(200);
            response.setError(false);
            response.setMessage(messageSource.getMessage("event.has.added", null, new Locale("en")));


        }else{
            response.setCode(200);
            response.setError(true);
            response.setMessage(messageSource.getMessage("event.has.not.added", null, new Locale("en")));

        }

        return response;
    }
    
    public EventsResponse getFinishedAndVerifiedEvents() {
    	
    	EventsResponse response = new EventsResponse();
    	
    	List<EventDto> listEventDto = new ArrayList<EventDto>();
    	
    	Set<Event> events = eventService.findByFinishedAndVerified(true, true);
    	
    	if (!events.isEmpty()) events.stream().forEach(event -> {
    		
    		EventDto dto = new EventDto(); 
    		
    		dto.setEventId(event.getEvent_id());
    		dto.setEventDescription(event.getEvent_desc());
    		dto.setEventName(event.getEvent_name());
    		dto.setImageURL(event.getLocation_url());

    		listEventDto.add(dto);
    		
    		
    	});
    	
    	response.setEvents(listEventDto);
        response.setStringResponse(new StringResponse(200, false, messageSource.getMessage("finished.verified", null, new Locale("en"))));

    	
    	return response;
    	
    }
    
    public EventsResponse getFinishedAndNotVerifiedEvents() {
    	
    	EventsResponse response = new EventsResponse();
    	
    	List<EventDto> listEventDto = new ArrayList<EventDto>();
    	
    	Set<Event> events = eventService.findByFinishedAndVerified(true, false);
    	
    	if (!events.isEmpty()) events.stream().forEach(event -> {
    		
    		EventDto dto = new EventDto(); 
    		
    		dto.setEventId(event.getEvent_id());
    		dto.setEventDescription(event.getEvent_desc());
    		dto.setEventName(event.getEvent_name());
    		dto.setImageURL(event.getLocation_url());

    		listEventDto.add(dto);
    		
    		
    	});
    	
    	response.setEvents(listEventDto);
        response.setStringResponse(new StringResponse(200, false, messageSource.getMessage("finished.non.verified", null, new Locale("en"))));

    	
    	return response;
    	
    }

    private static String getTodayDateTime(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return dtf.format(now);
    }

}
