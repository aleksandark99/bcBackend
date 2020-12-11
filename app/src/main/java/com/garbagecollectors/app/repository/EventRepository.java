package com.garbagecollectors.app.repository;

import com.garbagecollectors.app.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query(value = "Select * from events where is_organized_by_user_id = :userId", nativeQuery = true)
    Set<Event> findByUser (@Param("userId") int userId);


}
