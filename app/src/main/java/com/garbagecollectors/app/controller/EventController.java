package com.garbagecollectors.app.controller;

import java.io.IOException;
import java.util.Set;

import com.garbagecollectors.app.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.garbagecollectors.app.controller_impl.EventControllerImpl;
import com.garbagecollectors.app.model.Event;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/garbagecollectors")
public class EventController {

    @Autowired
    private EventControllerImpl eventControllerImpl;

    @GetMapping(value = "/event/{eventId}")
    public SingleEventDTO getEventById(@PathVariable("eventId") int eventId){

        return eventControllerImpl.getEventById(eventId);
    }

    @GetMapping(value = "/events/{userId}") //pristup: svi
    public Set<Event> getEventsByUser(@PathVariable("userId") int userId){

        return eventControllerImpl.getEventsByUser(userId);
    }

    @PostMapping(value = "user/event", consumes = {"multipart/form-data"})
    public StringResponse createEvent(@RequestParam("image") MultipartFile image, @ModelAttribute EventRequest event) throws IOException {

        return eventControllerImpl.createEvent(image, event);
    }
    
    @GetMapping(value = "events/finished/verified") //pristup: svi
    @ResponseBody
    public EventsResponse getFinishedAndVerifiedEvents() {
    	
    	EventsResponse response = eventControllerImpl.getFinishedAndVerifiedEvents();
    	
    	return response;
    	
    }

    @GetMapping(value = "events/unfinished/unverified")
    @ResponseBody
    public EventsResponse getUnfinishedAndUnverifiedEvents(){

        EventsResponse response = eventControllerImpl.getUnfinishedAndUnverifiedEvents();

        return response;
    }

    @DeleteMapping("/event/remove/{eventId}")
    public StringResponse deleteEvent(@PathVariable("eventId") int eventId){

        return eventControllerImpl.deleteEvent(eventId);
    }

    @GetMapping("/event/goingNumber/{id}")
    public UsersNumForEventDTO getUsersNumForEvent(@PathVariable("id") int eventId){

        return eventControllerImpl.getUsersNumForEvent(eventId);
    }

    @GetMapping("/events/all/{username}")
    public EventsResponse getEventsByUserName(@PathVariable("username") String username){

        return eventControllerImpl.getEventsByUsername(username);
    }

}
