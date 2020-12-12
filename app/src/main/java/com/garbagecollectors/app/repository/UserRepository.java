package com.garbagecollectors.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.garbagecollectors.app.dto.UserStatsDto;
import com.garbagecollectors.app.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByJwt(String jwt);

    User findByPassword(String password);
    
    @Query(name = "findScoreBoard", nativeQuery = true)
    List<UserStatsDto> findScoreBoard();

}
