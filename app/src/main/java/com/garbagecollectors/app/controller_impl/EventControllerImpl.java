package com.garbagecollectors.app.controller_impl;



import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
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
import com.garbagecollectors.app.dto.EventsResponse;
import com.garbagecollectors.app.dto.ImgBB;
import com.garbagecollectors.app.dto.SingleEventDTO;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.model.Event;
import com.garbagecollectors.app.model.Picture;
import com.garbagecollectors.app.model.Profile;
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

    public SingleEventDTO getEventById(int eventId){
    	
    	boolean going;
    	
    	SingleEventDTO eventDTO = new SingleEventDTO();
    	
    	String authHeader = hsr.getHeader("Authorization");
    	
    	if (authHeader == null ) { 
    		
    		going = false;
    		
    	}
        	
    	String token = authHeader.substring(7);
       
       	Event event = eventService.findEventById(eventId);
       
       	if (event == null) eventDTO.setStringResponse(new StringResponse(200, false, messageSource.getMessage("bad.event.id", null, new Locale("en"))));
        
     	User loggedInUser = userService.findByJwt(token);
       	
       	if (loggedInUser != null) {

			going = loggedInUser.getUser_events().contains(event);
    		
    		eventDTO.setGoing(going);
    		
    	}

        
        eventDTO.setDateCreated(event.getStart_date());
        eventDTO.setEventDescription(event.getEvent_desc());
        eventDTO.setEventName(event.getEvent_name());

        String imageAfterURL = (event.getEnd_picture() == null ? null : event.getEnd_picture().getPicture_url());
        String imageTeamURL =(event.getTeam_picture() == null ? null : event.getTeam_picture().getPicture_url());

        eventDTO.setImgAfterURL(imageAfterURL);
        eventDTO.setImgBeforeURL(event.getStart_picture().getPicture_url());
        eventDTO.setImgTeamURL(imageTeamURL);
        eventDTO.setLocationString(event.getEvent_location());
        eventDTO.setLocationURL(event.getLocation_url());
        eventDTO.setFinished(event.isFinished());

        Profile userProfile = event.getIsOrganizedBy().getUserProfile();

        eventDTO.setOrganizedBy(userProfile.getFirst_name() + " " + userProfile.getLast_name());
        eventDTO.setOrganisatorId(event.getIsOrganizedBy().getUser_id());

        return eventDTO;
    }

    public Set<Event> getEventsByUser(int userId){

        return eventService.findEventsByUser(userId);
    }

    public EventsResponse getUnfinishedAndUnverifiedEvents(){

        EventsResponse response = new EventsResponse();

        List<EventDto> listEventDto = new ArrayList<>();

        Set<Event> events = eventService.findByUnfinishedAndUnverified();

        if (events != null & !events.isEmpty()) {
        	
        	 events.stream().forEach(event -> {

                 EventDto dto = new EventDto();

                 dto.setEventId(event.getEvent_id());
                 dto.setEventDescription(event.getEvent_desc());
                 dto.setEventName(event.getEvent_name());
                 if (event.getStart_picture() != null) dto.setImageURLstart(event.getStart_picture().getPicture_url());

                 Profile userProfile = event.getIsOrganizedBy().getUserProfile();
                 dto.setOrganizedBy(userProfile.getFirst_name() + " " + userProfile.getLast_name());
                 dto.setUserId(event.getIsOrganizedBy().getUser_id());


                 listEventDto.add(dto);
             });
        }

        response.setEvents(listEventDto);
        response.setStringResponse(new StringResponse(200, false, messageSource.getMessage("finished.verified", null, new Locale("en"))));

        return response;
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
    		dto.setImageURLend(event.getEnd_picture().getPicture_url());

            Profile userProfile = event.getIsOrganizedBy().getUserProfile();
    		dto.setOrganizedBy(userProfile.getFirst_name() + " " + userProfile.getLast_name());
    		dto.setUserId(event.getIsOrganizedBy().getUser_id());

            dto.setSuccessfull(event.isSuccessfull());
    			
    		listEventDto.add(dto);
    		
    		
    	});
    	
    	response.setEvents(listEventDto);
        response.setStringResponse(new StringResponse(200, false, messageSource.getMessage("finished.verified", null, new Locale("en"))));

    	
    	return response;
    	
    }
    


    private static String getTodayDateTime(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return dtf.format(now);
    }

}
