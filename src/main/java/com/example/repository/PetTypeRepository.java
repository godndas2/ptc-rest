package com.example.repository;

import com.example.model.entity.PetType;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetTypeRepository extends JpaRepository<PetType, Integer> {
    PetType findById(int id) throws DataAccessException;
}
