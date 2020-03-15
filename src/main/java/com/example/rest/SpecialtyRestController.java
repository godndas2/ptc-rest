package com.example.rest;

import com.example.model.entity.Specialty;
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
import java.util.ArrayList;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/specialties")
public class SpecialtyRestController {

    private final ClinicService clinicService;

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Specialty> addSpecialty(@RequestBody @Valid Specialty specialty,
                                                  BindingResult bindingResult,
                                                  UriComponentsBuilder builder) {
        BindingErrorsResponse errorsResponse = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (specialty == null)) {
            errorsResponse.addAllErrors(bindingResult);
            headers.add("errors", errorsResponse.toJson());
            return new ResponseEntity<Specialty>(headers, HttpStatus.BAD_REQUEST);
        }
        this.clinicService.saveSpecialty(specialty);
        headers.setLocation(builder.path("/api/specialties/{id}").buildAndExpand(specialty.getId()).toUri());
        return new ResponseEntity<Specialty>(specialty, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<Specialty>> findAllSpecialties() {
        Collection<Specialty> specialties = new ArrayList<>();
        specialties.addAll(this.clinicService.findAllSpecialties());
        if (specialties.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(specialties, HttpStatus.OK);
    }

    @GetMapping(value = "{specId}", produces = "application/json")
    public ResponseEntity<Specialty> findBySpecialtyId(@PathVariable("specId") int specId) {
        Specialty specialty = this.clinicService.findBySpecialtyId(specId);
        if (specialty == null) {
            return new ResponseEntity<Specialty>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Specialty>(specialty, HttpStatus.OK);
    }

    @PutMapping(value = "/{specId}", produces = "application/json")
    public ResponseEntity<Specialty> updateSpecialty(@PathVariable("specId") int specId,
                                                     BindingResult bindingResult,
                                                     @RequestBody @Valid Specialty specialty) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if(bindingResult.hasErrors() || (specialty == null)){
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJson());
            return new ResponseEntity<Specialty>(headers, HttpStatus.BAD_REQUEST);
        }
        Specialty currentSpecialty = this.clinicService.findBySpecialtyId(specId);
        if (currentSpecialty == null) {
            return new ResponseEntity<Specialty>(HttpStatus.NOT_FOUND);
        }
        currentSpecialty.setName(specialty.getName());
        this.clinicService.saveSpecialty(currentSpecialty);
        return new ResponseEntity<Specialty>(currentSpecialty, HttpStatus.NO_CONTENT);
    }

    @Transactional
    @DeleteMapping(value = "/{spedId}", produces = "application/json")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable("specId") int specId) {
        Specialty specialty = this.clinicService.findBySpecialtyId(specId);
        if (specialty == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteBySpecialtyId(specialty);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
}
