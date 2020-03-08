package com.example.rest;

import com.example.model.entity.Owner;
import com.example.rest.response.BindingErrorsResponse;
import com.example.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/owners")
public class OwnerRestController {

    private final ClinicService clinicService;

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Owner> addOwner(@RequestBody @Valid Owner owner,
                                          BindingResult bindingResult,
                                          UriComponentsBuilder ucBuilder) {
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || owner.getId() != null) {
            BindingErrorsResponse errors = new BindingErrorsResponse(owner.getId());
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJson());
            return new ResponseEntity<Owner>(headers, HttpStatus.BAD_REQUEST);
        }
        this.clinicService.saveOwner(owner);
        headers.setLocation(ucBuilder.path("/api/owners/{id}").buildAndExpand(owner.getId()).toUri());
        return new ResponseEntity<Owner>(owner, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<Owner>> getOwners() {
        Collection<Owner> owners = this.clinicService.findAllOwners();
        if (owners.isEmpty()) {
            return new ResponseEntity<Collection<Owner>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Owner>>(owners, HttpStatus.OK);
    }


    @GetMapping(value = "/{ownerId}", produces = "application/json")
    public ResponseEntity<Owner> getOwner(@PathVariable("ownerId") int ownerId) {
        Owner owner = null;
        owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return new ResponseEntity<Owner>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Owner>(owner, HttpStatus.OK);
    }

    @GetMapping(value = "/*/lastname/{lastName}", produces = "application/json")
    public ResponseEntity<Collection<Owner>> getOwnersList(@PathVariable("lastName") String ownerLastName) {
        if (ownerLastName == null) {
            ownerLastName = "";
        }
        Collection<Owner> owners = this.clinicService.findOwnerByLastName(ownerLastName);
        if (owners.isEmpty()) {
            return new ResponseEntity<Collection<Owner>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Owner>>(owners, HttpStatus.OK);
    }

    @PutMapping(value = "/{ownerId}", produces = "application/json")
    public ResponseEntity<Owner> updateOwner(@PathVariable("ownerId") int ownerId,
                                             @RequestBody @Valid Owner owner,
                                             BindingResult bindingResult,
                                             UriComponentsBuilder ucBuilder) {

        boolean bodyIdMatchesPathId = owner.getId() == null || ownerId == owner.getId();

        if (bindingResult.hasErrors() || !bodyIdMatchesPathId) {
            BindingErrorsResponse errors = new BindingErrorsResponse(ownerId, owner.getId());
            errors.addAllErrors(bindingResult);
            HttpHeaders headers = new HttpHeaders();
            headers.add("errors", errors.toJson());
            return new ResponseEntity<Owner>(headers, HttpStatus.BAD_REQUEST);
        }
        Owner currentOwner = this.clinicService.findOwnerById(ownerId);
        if (currentOwner == null) {
            return new ResponseEntity<Owner>(HttpStatus.NOT_FOUND);
        }
        currentOwner.setAddress(owner.getAddress());
        currentOwner.setCity(owner.getCity());
        currentOwner.setFirstName(owner.getFirstName());
        currentOwner.setLastName(owner.getLastName());
        currentOwner.setTelephone(owner.getTelephone());
        this.clinicService.saveOwner(currentOwner);

        return new ResponseEntity<Owner>(currentOwner, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{ownerId}",produces = "application/json")
    @Transactional
    public ResponseEntity<Void> deleteOwner(@PathVariable("ownerId") int ownerId) {
        Owner owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteOwner(owner);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
