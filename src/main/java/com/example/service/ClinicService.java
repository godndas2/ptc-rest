package com.example.service;

import com.example.model.entity.*;
import com.example.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClinicService {

    private final OwnerRepository ownerRepository;
    private final PetRepository petRepository;
    private final PetTypeRepository petTypeRepository;
    private final VetRepository vetRepository;
    private final SpecialtyRepository specialtyRepository;
//    private final VisitRepository visitRepository;


    @Transactional
    public void saveOwner(Owner owner) throws DataAccessException {
        ownerRepository.save(owner);
    }

    @Transactional(readOnly = true)
    public Collection<Owner> findAllOwners() throws DataAccessException {
        return ownerRepository.findAll();
    }

    @Transactional(readOnly = true) //
    public Owner findOwnerById(int id) throws DataAccessException {
        Optional<Owner> owner = null;
        try {
            owner = ownerRepository.findById(id);
        } catch (ObjectRetrievalFailureException | EmptyResultDataAccessException e) {
            return null;
        }
        return owner.orElseThrow(RuntimeException::new);
    }

    @Transactional(readOnly = true)
    public Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException {
        return ownerRepository.findByLastName(lastName);
    }

    @Transactional
    public void deleteOwner(Owner owner) throws DataAccessException {
        ownerRepository.delete(owner);
    }

    @Transactional(readOnly = true)
    public Pet findPetById(int id) throws DataAccessException {
        Pet pet = null;
        try {
            pet = petRepository.findById(id);
        } catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
            return null;
        }
        return pet;
    }

    @Transactional(readOnly = true)
    public Collection<Pet> findAllPets() throws DataAccessException {
        return petRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Collection<PetType> findPetTypes() throws DataAccessException {
//        return petRepository.findPetTypes();
        return null;
    }

    @Transactional(readOnly = true)
    public PetType findPetTypeById(int petTypeId) {
        PetType petType = null;
        try {
            petType = petTypeRepository.findById(petTypeId);
        } catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
            return null;
        }
        return petType;
    }

    @Transactional
    public void savePet(Pet pet) throws DataAccessException {
        petRepository.save(pet);
    }

    @Transactional
    public void deletePet(Pet pet) throws DataAccessException {
        petRepository.delete(pet);
    }


    @Transactional
    public void savePetType(PetType petType) throws DataAccessException {
        petTypeRepository.save(petType);
    }

    @Transactional(readOnly = true)
    public Collection<PetType> findAllPetTypes() throws DataAccessException {
        return petTypeRepository.findAll();
    }

    @Transactional
    public void deletePetType(PetType petType) throws DataAccessException {
        petTypeRepository.delete(petType);
    }

    @Transactional
    public void saveVet(Vet vet) throws DataAccessException {
        vetRepository.save(vet);
    }

    @Transactional(readOnly = true)
    public Collection<Vet> getAllVets() throws DataAccessException {
        return vetRepository.findAll();
    }

    public Vet findByVetId(int vetId) throws DataAccessException {
        Optional<Vet> vet = null;
        try {
            vet = vetRepository.findById(vetId);
        } catch (ObjectRetrievalFailureException|EmptyResultDataAccessException e) {
            return null;
        }
        return vet.orElseThrow(null); //
    }

    @Transactional
    public void deleteVet(Vet vet) throws DataAccessException {
        vetRepository.delete(vet);
    }

    @Transactional
    public void saveSpecialty(Specialty specialty) {
        specialtyRepository.save(specialty);

    }

    @Transactional(readOnly = true)
    public Collection<Specialty> findAllSpecialties() throws DataAccessException {
        return specialtyRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Specialty findBySpecialtyId(int specId) throws DataAccessException {
        Optional<Specialty> specialty = null;
        try {
            specialty = specialtyRepository.findById(specId);
        } catch (ObjectRetrievalFailureException | EmptyResultDataAccessException e) {
            return null;
        }
        return specialty.orElseThrow(null); //
    }

    @Transactional
    public void deleteBySpecialtyId(Specialty specialty) throws DataAccessException {
        specialtyRepository.delete(specialty);
    }


}
