package com.example.rest;

import com.example.model.User;
import com.example.rest.response.BindingErrorsResponse;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
@CrossOrigin(exposedHeaders = "errors, content-type") // Header 에서 노출할 수 있도록
public class UserRestController {

    private final UserService userService;

    @PreAuthorize("hasRole(@roles.ADMIN)") // TODO
    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<User> addOwner(@RequestBody @Valid User user,
                                           BindingResult bindingResult) throws Exception {
        BindingErrorsResponse errors = new BindingErrorsResponse();
        HttpHeaders headers = new HttpHeaders();
        if (bindingResult.hasErrors() || (user == null)) {
            errors.addAllErrors(bindingResult);
            headers.add("errors", errors.toJson());
            return new ResponseEntity<>(user, headers, HttpStatus.BAD_REQUEST);
        }

        this.userService.saveUser(user);
        return new ResponseEntity<>(user, headers, HttpStatus.CREATED);
    }

}
