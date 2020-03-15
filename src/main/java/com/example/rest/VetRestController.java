package com.example.rest;

import com.example.model.entity.Specialty;
import com.example.model.entity.Vet;
import com.example.rest.response.BindingErrorsResponse;
import com.example.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
@RequestMapping("api/vets")
public class VetRestController {

    private final ClinicService clinicService;

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<Vet> addVet(@RequestBody @Valid Vet vet,
                                      BindingResult bindingResult,
                                      UriComponentsBuilder ucBuilder){
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();

        if(bindingResult.hasErrors() || (vet == null)){
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJson());
            return new ResponseEntity<Vet>(headers, HttpStatus.BAD_REQUEST);
        }
        this.clinicService.saveVet(vet);
        headers.setLocation(ucBuilder.path("/api/vets/{id}").buildAndExpand(vet.getId()).toUri()); // http://localhost/api/vets/999
        return new ResponseEntity<Vet>(vet, headers, HttpStatus.CREATED);
    }

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<Vet>> getAllVets() {
        Collection<Vet> allVets = this.clinicService.getAllVets();
        if (allVets.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(allVets, HttpStatus.OK);
    }

    // TODO Junit <BEGIN>
    @GetMapping(value = "{vetId}", produces = "application/json")
    public ResponseEntity<Vet> getVet(@PathVariable("vetId") int vetId) {
        Vet vet = this.clinicService.findByVetId(vetId);
        if (vet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(vet, HttpStatus.OK);
    }

    @PutMapping(value = "/{vetId}", produces = "application/json")
    public ResponseEntity<Vet> updateVet(@PathVariable("vetId") int vetId,
                                         BindingResult bindingResult,
                                         @RequestBody @Valid Vet vet) {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if(bindingResult.hasErrors() || (vet == null)){
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJson());
            return new ResponseEntity<Vet>(headers, HttpStatus.BAD_REQUEST);
        }
        Vet currentVet = this.clinicService.findByVetId(vetId);
        if(currentVet == null){
            return new ResponseEntity<Vet>(HttpStatus.NOT_FOUND);
        }
        currentVet.setFirstName(vet.getFirstName());
        currentVet.setLastName(vet.getLastName());
        currentVet.clearSpecialties();
        for(Specialty spec : vet.getSpecialties()) {
            currentVet.addSpecialty(spec);
        }
        this.clinicService.saveVet(currentVet);
        return new ResponseEntity<Vet>(currentVet, HttpStatus.NO_CONTENT);
    }

    @Transactional
    @DeleteMapping(value = "{vetId}", produces = "application/json")
    public ResponseEntity<Void> deleteVet(@PathVariable("vetId") int vetId) {
        Vet vet = this.clinicService.findByVetId(vetId);
        if (vet == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
        this.clinicService.deleteVet(vet);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }
    // TODO <END>
}
