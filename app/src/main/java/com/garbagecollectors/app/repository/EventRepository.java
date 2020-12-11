package com.garbagecollectors.app.repository;

import com.garbagecollectors.app.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    @Query("Select * from events e where e.isOrganizedBy = ?1")
    Set<Event> findByUser (int userId);


}
