package com.example.repository;

import com.example.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {

//    void save(User user) throws DataAccessException;
}
