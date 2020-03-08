package com.example.rest;

import com.example.model.entity.Owner;
import com.example.rest.response.BindingErrorsResponse;
import com.example.service.ClinicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("/api/owners")
public class OwnerRestController {

    private final ClinicService clinicService;

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
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

    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<Owner>> getOwners() {
        Collection<Owner> owners = this.clinicService.findAllOwners();
        if (owners.isEmpty()) {
            return new ResponseEntity<Collection<Owner>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<Owner>>(owners, HttpStatus.OK);
    }


    @PreAuthorize( "hasRole(@roles.OWNER_ADMIN)" )
    @GetMapping(value = "/{ownerId}", produces = "application/json")
    public ResponseEntity<Owner> getOwner(@PathVariable("ownerId") int ownerId) {
        Owner owner = null;
        owner = this.clinicService.findOwnerById(ownerId);
        if (owner == null) {
            return new ResponseEntity<Owner>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Owner>(owner, HttpStatus.OK);
    }



}
