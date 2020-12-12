package com.garbagecollectors.app.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.garbagecollectors.app.dto.EventForUserDto;
import com.garbagecollectors.app.dto.UserStatsDto;
import com.garbagecollectors.app.model.User;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByJwt(String jwt);

    User findByPassword(String password);
    
    @Query(name = "findScoreBoard", nativeQuery = true)
    List<UserStatsDto> findScoreBoard();
    
    @Query(name = "findEventsForUser", nativeQuery = true)
    List<EventForUserDto> findEventsForUser(@Param("userId") int userId);

}
