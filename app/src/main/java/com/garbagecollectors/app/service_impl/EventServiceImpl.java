package com.garbagecollectors.app.service_impl;

import com.garbagecollectors.app.model.Event;
import com.garbagecollectors.app.repository.EventRepository;
import com.garbagecollectors.app.service.EventService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository repository;

    @Inject
    public EventServiceImpl(final EventRepository repository){this.repository=repository;}

    @Override
    public Event save(Event event) {
        return  repository.save(event);
    }

    @Override
    public Event findEventById(int eventId) {
        return repository.findById(eventId).get();
    }

    @Override
    public Set<Event> findEventsByUser(int userId) {
        return repository.findByUser(userId);
    }


	@Override
	public Set<Event> findByFinishedAndVerified(boolean finished, boolean verified) {
		return repository.findByFinishedAndVerified(finished, verified);
	}

    @Override
    public Set<Event> findByUnfinishedAndUnverified() {
        return repository.findByFinishedIsFalseAndVerifiedIsFalse();
    }

	@Override
	public void verifyAndSetIsSuccessful(boolean successful, int eventId) {
		repository.verifyAndSetIsSuccessful(successful, eventId);
		return;
		
	}

    @Override
    public void delete(int eventId) {

        repository.deleteById(eventId);
        return;
    }

    @Override
    public int getUsersNumForEvent(int eventId) {

        return repository.getUsersNumForEvent(eventId);
    }


}
