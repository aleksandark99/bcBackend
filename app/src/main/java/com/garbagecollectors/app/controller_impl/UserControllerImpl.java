package com.garbagecollectors.app.controller_impl;


import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import com.garbagecollectors.app.dto.ConfirmeRequest;
import com.garbagecollectors.app.dto.IdRequest;
import com.garbagecollectors.app.dto.ImgBB;
import com.garbagecollectors.app.dto.LoginRequest;
import com.garbagecollectors.app.dto.LoginResponse;
import com.garbagecollectors.app.dto.RegisterRequest;
import com.garbagecollectors.app.dto.ScoreBoardResponse;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.dto.UserStatsDto;
import com.garbagecollectors.app.model.Event;
import com.garbagecollectors.app.model.Picture;
import com.garbagecollectors.app.model.Profile;
import com.garbagecollectors.app.model.User;
import com.garbagecollectors.app.model.enums.ERole;
import com.garbagecollectors.app.security.JwtUtil;
import com.garbagecollectors.app.service.EventService;
import com.garbagecollectors.app.service.UserService;
import com.garbagecollectors.app.utility.UtilityHelper;

@Service
public class UserControllerImpl {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private EventService eventService;
    
    @Autowired
    private HttpServletRequest hsr;

    @Value("${secret.key}")
    private String secret;
    
    private static String UPLOAD_ROOT = "https://api.imgbb.com/1/upload?key=b06a3582d53c4a0be976670478081f5c";


    public StringResponse register(RegisterRequest request){

        StringResponse response = new StringResponse();

        User existingUser = userService.findByUsername(request.getUsername());

        if (existingUser == null){


            User newUser = new User();
            newUser.setUsername(request.getUsername());
            newUser.setPassword(UtilityHelper.hashingPassword(request.getPassword()).toString());
            newUser.setUser_role(ERole.USER);

            //samo se registruju useri, admine hardkodujemo u bazi

            Profile profile = new Profile();
            profile.setFirst_name(request.getFirstName());
            profile.setLast_name(request.getLastName());

            newUser.setUserProfile(profile);

            userService.save(newUser);

            response.setCode(200);
            response.setError(false);
            response.setMessage(messageSource.getMessage("user.registered", null, new Locale("en")));

        } else {

            response.setCode(200);
            response.setError(true);
            response.setMessage(messageSource.getMessage("user.not.registered", null, new Locale("en")));
        }

        return  response;
    }

    public LoginResponse login(LoginRequest request){

        User user = userService.findByUsername(request.getUsername());
        User user1 = userService.findByPassword(UtilityHelper.hashingPassword(request.getPassword()).toString());
        LoginResponse response = new LoginResponse();

        if (user1 == null) System.out.println("problem");

        if(user == user1){

                String jwt = jwtUtil.generateToken(user.getUsername());
                user.setJwt(jwt);

                userService.save(user);
                
                response.setRole(String.valueOf(user.getUser_role()));
                response.setJwt(user.getJwt());
                response.setStringResponse(new StringResponse(200, false, messageSource.getMessage("good.credentials", null, new Locale("en"))));

        } else {
            response.setStringResponse(new StringResponse(200, true, messageSource.getMessage("bad.credentials", null, new Locale("en"))));
        }

        return  response;
    }
    
    public StringResponse confirmePresence(ConfirmeRequest request) {
    	
    	return null;
    	
    }
    
    public ScoreBoardResponse getScoreBoard() {
    	
    	List<UserStatsDto> users = null;
    	
    	ScoreBoardResponse response = new ScoreBoardResponse();
    	
    	users = userService.findScoreBoard();
    	
    	if (users == null) {
    		response.setResponse(new StringResponse(200, true,  messageSource.getMessage("fetch.error", null, new Locale("en"))));
    	} else {
    		response.setUsers(users);
    		response.setResponse(new StringResponse(200, true,  messageSource.getMessage("fetch.scoreboard", null, new Locale("en"))));

    	}
    	
    	return response;
    }
    
    public StringResponse finishEvent(MultipartFile afterImage, MultipartFile teamImage, IdRequest eventId) throws IOException {
    	
    	StringResponse response = new StringResponse();
    	
    	String afterImageUrl = uploadImage(afterImage);
    	
    	
    	if (afterImageUrl != null) {
    		
    		String teamImageUrl = uploadImage(teamImage);
    		
    		if(teamImageUrl != null) {
    			
    			Event event = eventService.findEventById(eventId.getId());
    			
    			if (event != null) {
    				
    				Picture afterPicture = new Picture();
    				afterPicture.setPicture_url(afterImageUrl);
    				afterPicture.setPicture_name(afterImage.getOriginalFilename());
    				afterPicture.setPicture_size(afterImage.getSize());
    				afterPicture.setMime_type(afterImage.getContentType());
    				
    				event.setEnd_picture(afterPicture);
    				
    				Picture teamPicture = new Picture();
    				teamPicture.setPicture_url(teamImageUrl);
    				teamPicture.setPicture_name(teamImage.getOriginalFilename());
    				teamPicture.setPicture_size(teamImage.getSize());
    				teamPicture.setMime_type(teamImage.getContentType());
    				
    				event.setTeam_picture(teamPicture);

    				teamPicture.setEventEnd(event);
    				afterPicture.setEventTeam(event);
    				event.setFinished(true);
    				eventService.save(event);
    				
    				response.setCode(200);
    	            response.setError(false);
    	            response.setMessage(messageSource.getMessage("event.finished", null, new Locale("en")));
    				
    			} else return new StringResponse(200, true,  messageSource.getMessage("bad.event.id", null, new Locale("en")));
    			
    			
    		} else {
    			response.setCode(200);
                response.setError(true);
                response.setMessage(messageSource.getMessage("team.upload.fail", null, new Locale("en")));
    		}
    		
    		
    	} else {
    		
    		 response.setCode(200);
             response.setError(true);
             response.setMessage(messageSource.getMessage("after.upload.fail", null, new Locale("en")));
    	}
    	
    	return response;
    }
    
    private String uploadImage(MultipartFile image) throws IOException {
    	if(!image.isEmpty()) {

            MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<String, Object>();
            
            bodyMap.add("image", Base64.getEncoder().encode(image.getBytes()));

            HttpHeaders httpHeaders = new HttpHeaders();

            httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String,Object>>(bodyMap, httpHeaders);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<ImgBB> responseEntity = restTemplate.exchange(UPLOAD_ROOT, HttpMethod.POST, requestEntity, ImgBB.class);

            String pic_url = responseEntity.getBody().getData().getDisplay_url();
            
            return pic_url;
    	} else {
    		
			return null;
		}
    	
    }


}
