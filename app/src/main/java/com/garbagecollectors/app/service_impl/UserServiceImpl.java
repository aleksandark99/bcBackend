package com.garbagecollectors.app.service_impl;


import com.garbagecollectors.app.model.User;
import com.garbagecollectors.app.repository.UserRepository;
import com.garbagecollectors.app.service.UserService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Inject
    public UserServiceImpl(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        return repository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username);
    }

    @Override
    public User findByJwt(String jwt) {
        return repository.findByJwt(jwt);
    }

    @Override
    public User findByPassword(String password) {
        return repository.findByPassword(password);
    }
}
