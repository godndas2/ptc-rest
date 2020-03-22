package com.example.repository;

import com.example.model.entity.Document;
import com.example.model.entity.Owner;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface OwnerRepository extends JpaRepository<Owner, Integer> {
    Collection<Owner> findByLastName(String lastName) throws DataAccessException;
}
