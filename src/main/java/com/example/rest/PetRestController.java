package com.example.rest;

import com.example.model.entity.Pet;
import com.example.model.entity.PetType;
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

import javax.net.ssl.HttpsURLConnection;
import javax.validation.Valid;
import javax.xml.ws.RespectBinding;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@CrossOrigin(exposedHeaders = "errors, content-type")
@RequestMapping("api/pets")
public class PetRestController {

    private final ClinicService clinicService;

    @GetMapping(value = "{petId}", produces = "application/json")
    public ResponseEntity<Pet> getPet(@PathVariable("petId") int petId) {
        Pet pet = this.clinicService.findPetById(petId);
        if (pet == null) {
            return new ResponseEntity<Pet>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<Pet>> getPets() {
        Collection<Pet> pets = this.clinicService.findAllPets();
        if (pets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pets, HttpStatus.OK);
    }

    @GetMapping(value = "/pettypes", produces = "application/json")
    public ResponseEntity<Collection<PetType>> getPetTypes() {
        return new ResponseEntity<Collection<PetType>>(this.clinicService.findPetTypes(), HttpStatus.OK);
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Pet> addPet(@RequestBody @Valid Pet pet,
                                      BindingResult bindingResult,
                                      UriComponentsBuilder ucBuilder){
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if(bindingResult.hasErrors() || (pet == null)){
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJson());
            return new ResponseEntity<Pet>(headers, HttpStatus.BAD_REQUEST);
        }
        this.clinicService.savePet(pet);
        headers.setLocation(ucBuilder.path("/api/pets/{id}").buildAndExpand(pet.getId()).toUri());
        return new ResponseEntity<Pet>(pet, headers, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{petId}", produces = "application/json")
    public ResponseEntity<Pet> updatePet(@PathVariable("petId") int petId,
                                         @RequestBody @Valid Pet pet,
                                         BindingResult bindingResult) {
        BindingErrorsResponse errorsResponse = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (pet == null)) {
            errorsResponse.addAllErrors(bindingResult);
            headers.add("errors", errorsResponse.toJson());
            return new ResponseEntity<>(headers, HttpStatus.BAD_REQUEST);
        }
        Pet currentPet = this.clinicService.findPetById(petId);
        if (currentPet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        currentPet.setBirthDate(pet.getBirthDate());
        currentPet.setName(pet.getName());
        currentPet.setType(pet.getType());
        currentPet.setOwner(pet.getOwner());
        this.clinicService.savePet(currentPet);
        return new ResponseEntity<>(currentPet, HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{petId}", produces = "application/json")
    @Transactional
    public ResponseEntity<Void> deletePet(@PathVariable("petId") int petId){
        Pet pet = this.clinicService.findPetById(petId);
        if(pet == null){
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deletePet(pet);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }





}
