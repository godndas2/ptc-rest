package com.example.rest;

import com.example.model.entity.PetType;
import com.example.rest.response.BindingErrorsResponse;
import com.example.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.net.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Collection;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequiredArgsConstructor
@RequestMapping("api/pettypes")
public class PetTypeRestController {

    private final ClinicService clinicService;

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<PetType> addPetType(@RequestBody @Valid PetType petType,
                                              BindingResult bindingResult,
                                              UriComponentsBuilder uriComponentsBuilder) {
        BindingErrorsResponse errorsResponse = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (petType == null)) {
            errorsResponse.addAllErrors(bindingResult);
            headers.add("errors", errorsResponse.toJson());
            return new ResponseEntity<PetType>(headers, HttpStatus.BAD_REQUEST);
        }
        this.clinicService.savePetType(petType);
        headers.setLocation(uriComponentsBuilder.path("/api/pettypes/{id}").buildAndExpand(petType.getId()).toUri());
        return new ResponseEntity<>(petType, HttpStatus.CREATED);
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<PetType>> getAllPetTypes() {
        Collection<PetType> petTypes = new ArrayList<>();
        petTypes.addAll(this.clinicService.findAllPetTypes());
        if (petTypes.isEmpty()) {
            return new ResponseEntity<Collection<PetType>>(petTypes, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Collection<PetType>>(petTypes, HttpStatus.OK);
    }

    @GetMapping(value = "/{petTypeId}", produces = "application/json")
    public ResponseEntity<PetType> getPetType(@PathVariable("petTypeId") int petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return new ResponseEntity<PetType>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(petType, HttpStatus.OK);
    }

    @PutMapping(value = "{petTypeId}", produces = "application/json")
    public ResponseEntity<PetType> updatePetType(@PathVariable("petTypeId") int petTypeId,
                                                 @RequestBody @Valid PetType petType,
                                                 BindingResult bindingResult) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if(bindingResult.hasErrors() || (petType == null)){
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJson());
            return new ResponseEntity<PetType>(headers, HttpStatus.BAD_REQUEST);
        }
        PetType currentPetType = this.clinicService.findPetTypeById(petTypeId);
        if (currentPetType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentPetType.setName(petType.getName());
        this.clinicService.savePetType(currentPetType);
        return new ResponseEntity<>(currentPetType, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "{petTypeId}", produces = "application/json")
    @Transactional
    public ResponseEntity<Void> deletePetType(@PathVariable("petTypeId") int petTypeId) {
        PetType petType = this.clinicService.findPetTypeById(petTypeId);
        if (petType == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deletePetType(petType);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }



}
