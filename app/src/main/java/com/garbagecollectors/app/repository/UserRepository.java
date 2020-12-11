package com.garbagecollectors.app.repository;

import com.garbagecollectors.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);

    User findByJwt(String jwt);

    User findByPassword(String password);

}
