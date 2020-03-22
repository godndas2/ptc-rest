package com.example.rest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class UploadController {

    public static final String uploadingDir = System.getProperty("user.dir") + "/uploadingDir/";

    @GetMapping("/upload")
    public String uploading(Model model) throws IOException {
        File file = null;
        new File(UploadController.uploadingDir).mkdirs();
        file = new File(uploadingDir);
        model.addAttribute("files", file.listFiles());
        return "uploading";
    }

    @PostMapping("/upload")
    public String uploadingPost(@RequestParam("uploadingFiles") MultipartFile[] uploadingFiles) throws IOException {
        for(MultipartFile uploadedFile : uploadingFiles) {
            File file = new File(uploadingDir + uploadedFile.getOriginalFilename());
            uploadedFile.transferTo(file);
        }
        return "redirect:/";
    }
}
