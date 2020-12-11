package com.garbagecollectors.app.controller_impl;


import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.garbagecollectors.app.dto.ConfirmeRequest;
import com.garbagecollectors.app.dto.LoginRequest;
import com.garbagecollectors.app.dto.LoginResponse;
import com.garbagecollectors.app.dto.RegisterRequest;
import com.garbagecollectors.app.dto.ScoreBoardResponse;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.model.Profile;
import com.garbagecollectors.app.model.User;
import com.garbagecollectors.app.model.enums.ERole;
import com.garbagecollectors.app.security.JwtUtil;
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
    

    @Value("${secret.key}")
    private String secret;


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
    	
    	return null;
    }

}
