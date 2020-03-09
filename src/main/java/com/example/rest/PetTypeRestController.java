package com.example.rest;

import com.example.model.entity.PetType;
import com.example.rest.response.BindingErrorsResponse;
import com.example.service.ClinicService;
import com.sun.deploy.net.HttpResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

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



}
