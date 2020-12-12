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
    



}
