package com.example.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@CrossOrigin(exposedHeaders = "errors, content-type")
public class IndexRestController {

    @GetMapping("/")
    public void indexSwagger(HttpServletResponse response) throws IOException {
        response.sendRedirect("/swagger-ui.html");
    }
}
