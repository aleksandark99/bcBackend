package com.garbagecollectors.app.controller;

import com.garbagecollectors.app.controller_impl.EventControllerImpl;
import com.garbagecollectors.app.dto.EventRequest;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.model.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/garbagecollectors")
public class EventController {

    @Autowired
    private EventControllerImpl eventControllerImpl;

    @GetMapping(value = "/event/{eventId}")
    public Event getEventById(@PathVariable("eventId") int eventId){

        return eventControllerImpl.getEventById(eventId);
    }

    @GetMapping(value = "/events/{userId}")
    public Set<Event> getEventsByUser(@PathVariable("userId") int userId){

        return eventControllerImpl.getEventsByUser(userId);
    }

    @PostMapping(value = "user/event", consumes = {"multipart/form-data"})
    public StringResponse createEvent(@RequestParam("image") MultipartFile image, @ModelAttribute EventRequest event) throws IOException {

        return eventControllerImpl.createEvent(image, event);
    }


}
