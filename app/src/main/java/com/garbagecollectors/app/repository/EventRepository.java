package com.garbagecollectors.app.repository;

import com.garbagecollectors.app.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "Select * from events where is_organized_by_user_id = :userId", nativeQuery = true)
    Set<Event> findByUser (@Param("userId") int userId);

    Set<Event> findByFinishedAndVerified(boolean finished, boolean verified);

    Set<Event> findByFinishedIsFalseAndVerifiedIsFalse();
    
    @Transactional
    @Modifying
    @Query(value = "UPDATE Event e SET e.verified = TRUE, e.successfull = ?1 WHERE e.event_id = ?2")
    void verifyAndSetIsSuccessful(boolean successfull, int eventId);
    
    @Query(value = "Select count(*) user_id from user_events where event_id = ?1", nativeQuery = true)
    int getUsersNumForEvent(int eventId);


    @Query(value = "Select * from events e where e.event_id in (select event_id from user_events ue join users u " +
            "on ue.user_id = u.user_id where u.username = ?1);", nativeQuery = true)
    Set<Event> findByUsername(String username);

}
