package com.garbagecollectors.app.service;

import java.util.List;

import com.garbagecollectors.app.dto.UserStatsDto;
import com.garbagecollectors.app.model.User;

public interface UserService {

    User save(User user);

    User findByUsername(String username);

    User findByJwt(String jwt);

    User findByPassword(String password);
    
    List<UserStatsDto> findScoreBoard();
}
