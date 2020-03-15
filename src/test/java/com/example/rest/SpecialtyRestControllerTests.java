package com.example.rest;

import com.example.exception.ExceptionControllerAdvice;
import com.example.model.entity.Specialty;
import com.example.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class SpecialtyRestControllerTests {

    @MockBean
    private ClinicService clinicService;
    @Autowired
    private SpecialtyRestController controller;

    private MockMvc mockMvc;
    private List<Specialty> specialties;


    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
        specialties = new ArrayList<>();

        Specialty specialty = new Specialty();
        specialty.setId(1);
        specialty.setName("radiology");
        specialties.add(specialty);

        specialty = new Specialty();
        specialty.setId(2);
        specialty.setName("surgery");
        specialties.add(specialty);

        specialty = new Specialty();
        specialty.setId(3);
        specialty.setName("dentistry");
        specialties.add(specialty);
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void getAllSpecialties() throws Exception {
        given(this.clinicService.findAllSpecialties()).willReturn(specialties);
        this.mockMvc.perform(get("/api/specialties/")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.[0].id").value(1))
        .andExpect(jsonPath("$.[0].name").value("radiology"))
        .andExpect(jsonPath("$.[1].id").value(2))
        .andExpect(jsonPath("$.[1].name").value("surgery"))
        .andDo(print());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void createSpecialty() throws Exception {
        Specialty specialty = specialties.get(0);
        specialty.setId(999);
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsString = objectMapper.writeValueAsString(specialty);
        this.mockMvc.perform(post("/api/specialties/")
        .content(writeValueAsString)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andDo(print());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void findBySpecialtyId() throws Exception {
        given(this.clinicService.findBySpecialtyId(1)).willReturn(specialties.get(0));
        this.mockMvc.perform(get("/api/specialties/1")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("radiology"))
        .andDo(print());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void updateSpecialty() throws Exception {
        given(this.clinicService.findBySpecialtyId(2)).willReturn(specialties.get(1));
        Specialty newSpecialty = specialties.get(1);
        newSpecialty.setName("newName");
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsString = objectMapper.writeValueAsString(newSpecialty);
        this.mockMvc.perform(put("/api/specialties/2")
        .content(writeValueAsString)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().contentType("application/json"))
        .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/api/specialties/2")
        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.name").value("newName"))
        .andDo(print());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void deleteSpecialty() throws Exception {
        Specialty delSpecialty = specialties.get(0);
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsString = objectMapper.writeValueAsString(delSpecialty);
        given(this.clinicService.findBySpecialtyId(1)).willReturn(specialties.get(0));
        this.mockMvc.perform(delete("/api/specialties/1")
        .content(writeValueAsString)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNoContent());
    }



}
