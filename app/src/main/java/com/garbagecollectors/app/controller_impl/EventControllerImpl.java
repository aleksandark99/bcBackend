package com.garbagecollectors.app.controller_impl;

import com.garbagecollectors.app.dto.EventRequest;
import com.garbagecollectors.app.dto.ImgBB;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.model.Event;
import com.garbagecollectors.app.model.Picture;
import com.garbagecollectors.app.model.User;
import com.garbagecollectors.app.service.EventService;
import com.garbagecollectors.app.service.PictureService;
import com.garbagecollectors.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import java.util.Set;

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

    private static String getTodayDateTime(){

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        return dtf.format(now);
    }

}
