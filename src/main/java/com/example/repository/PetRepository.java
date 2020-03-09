package com.example.repository;

import com.example.model.entity.Pet;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Integer> {
    Pet findById(int id) throws DataAccessException;

}
