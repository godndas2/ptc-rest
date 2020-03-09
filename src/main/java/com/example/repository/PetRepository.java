package com.example.repository;

import com.example.model.entity.Pet;
import com.example.model.entity.PetType;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet,Integer> {
    Pet findById(int id) throws DataAccessException;
    List<PetType> findPetTypes() throws DataAccessException; // Issue
    void delete(Pet pet) throws DataAccessException;
}
