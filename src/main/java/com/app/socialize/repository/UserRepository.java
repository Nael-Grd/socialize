package com.app.socialize.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.app.socialize.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
