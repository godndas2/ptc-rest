package com.example.service;

import com.example.model.entity.Owner;
import com.example.repository.OwnerRepository;
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
//    private final PetRepository petRepository;
//    private final VetRepository vetRepository;
//    private final VisitRepository visitRepository;
//    private final SpecialtyRepository specialtyRepository;
//    private final PetTypeRepository petTypeRepository;

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

}
