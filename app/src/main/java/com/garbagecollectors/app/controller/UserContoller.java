package com.garbagecollectors.app.controller;


import com.garbagecollectors.app.controller_impl.UserControllerImpl;
import com.garbagecollectors.app.dto.LoginRequest;
import com.garbagecollectors.app.dto.LoginResponse;
import com.garbagecollectors.app.dto.RegisterRequest;
import com.garbagecollectors.app.dto.StringResponse;
import com.garbagecollectors.app.model.User;
import com.garbagecollectors.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping(value = "user/test")
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
}
