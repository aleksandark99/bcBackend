package com.garbagecollectors.app.controller;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.garbagecollectors.app.controller_impl.UserControllerImpl;
import com.garbagecollectors.app.dto.ConfirmeRequest;
import com.garbagecollectors.app.dto.IdRequest;
import com.garbagecollectors.app.dto.LoginRequest;
import com.garbagecollectors.app.dto.LoginResponse;
import com.garbagecollectors.app.dto.RegisterRequest;
import com.garbagecollectors.app.dto.ScoreBoardResponse;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.model.User;
import com.garbagecollectors.app.service.UserService;

@RestController
@RequestMapping("/garbagecollectors")
public class UserContoller {

    @Autowired
    private UserControllerImpl userControllerImpl;

    @Autowired
    private HttpServletRequest hsr;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/register")
    @ResponseBody
    public StringResponse register(@RequestBody RegisterRequest request){

        StringResponse response = userControllerImpl.register(request);

        return response;

    }

    @PostMapping(value = "/login")
    @ResponseBody
    public LoginResponse login(@RequestBody LoginRequest request){

        LoginResponse response = userControllerImpl.login(request);

        return response;

    }

    @PostMapping(value = "/user/test")
    @ResponseBody
    public StringResponse test(){

        String jwtToken = hsr.getHeader("Authorization").substring(7);
        User user = userService.findByJwt(jwtToken);

        if (user != null){

            return  new StringResponse(200, false, "autentifikovan");

        } else {
            return new StringResponse(401, true, "ne autentifikovan");

        }

    }
    
    @PostMapping(value = "user/confirmePresence")
    @ResponseBody
    public StringResponse confirmePresence(@RequestBody ConfirmeRequest request) {
    	
    	StringResponse response = userControllerImpl.confirmePresence(request);
    	
    	return response;
    	
    }
    
    @GetMapping("/scoreBoard")
    public ScoreBoardResponse getScoreBoard() {
    	
    	ScoreBoardResponse response = userControllerImpl.getScoreBoard();
    	
    	return response;
		
	}
    
    @PostMapping(value = "user/finish/event", consumes = {"multipart/form-data"})
    public StringResponse finishEvent(@RequestParam("afterImage") MultipartFile afterImage, @RequestParam("teamImage") MultipartFile teamImage, @ModelAttribute IdRequest eventId) throws IOException {
    	
    	StringResponse response = userControllerImpl.finishEvent(afterImage, teamImage, eventId);
    	
    	return response;
    	
    }
    

    
}
