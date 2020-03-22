package com.example;

import com.example.rest.UploadController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class PtcRestApplication {

    public static void main(String[] args) throws IOException {
        new File(UploadController.uploadingDir).mkdirs();
        SpringApplication.run(PtcRestApplication.class, args);
    }

}
