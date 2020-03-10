package com.example.rest;

import com.example.exception.ExceptionControllerAdvice;
import com.example.model.entity.PetType;
import com.example.repository.PetTypeRepository;
import com.example.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
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
public class PetTypeRestControllerTests {

    @Autowired
    private PetTypeRestController petTypeRestController;
    @Autowired
    private PetTypeRepository petTypeRepository;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;

    private List<PetType> petTypes;

    @BeforeEach
    public void initPetTypes(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(petTypeRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();
        petTypes = new ArrayList<PetType>();

        PetType petType = new PetType();
        petType.setId(1);
        petType.setName("cat");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(2);
        petType.setName("dog");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(3);
        petType.setName("lizard");
        petTypes.add(petType);

        petType = new PetType();
        petType.setId(4);
        petType.setName("snake");
        petTypes.add(petType);
    }

    @AfterEach
    public void tearDown() {
        petTypeRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void createPetTypeSuccess() throws Exception {
        PetType newPetType = petTypes.get(0);
        newPetType.setId(999);
        ObjectMapper objectMapper = new ObjectMapper();
        String newPetTypeAsJson = objectMapper.writeValueAsString(newPetType);
        this.mockMvc.perform(post("/api/pettypes/")
        .content(newPetTypeAsJson).accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void createPetTypeError() throws Exception {
        PetType newPetType = petTypes.get(0);
        newPetType.setId(null);
        newPetType.setName(null);
        ObjectMapper objectMapper = new ObjectMapper();
        String newPetTypeAsJson = objectMapper.writeValueAsString(newPetType);
        this.mockMvc.perform(post("/api/pettypes/")
        .content(newPetTypeAsJson)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void getPetTypeSuccessAsOwnerAdmin() throws Exception {
        given(this.clinicService.findPetTypeById(1)).willReturn(petTypes.get(0));
        this.mockMvc.perform(get("/api/pettypes/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void getPetTypeSuccessAsVetAdmin() throws Exception {
        given(this.clinicService.findPetTypeById(1)).willReturn(petTypes.get(0));
        this.mockMvc.perform(get("/api/pettypes/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("cat"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    public void getPetTypeNotFound() throws Exception {
        given(this.clinicService.findPetTypeById(-1)).willReturn(null);
        this.mockMvc.perform(get("/api/pettpyes/-1")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    public void gAllPetTypesSuccessAsOwnerAdmin() throws Exception {
        petTypes.remove(0);
        petTypes.remove(1);
        given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get("/api/pettypes/")
        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(2))
                .andExpect(jsonPath("$.[0].name").value("dog"))
                .andExpect(jsonPath("$.[1].id").value(4))
                .andExpect(jsonPath("$.[1].name").value("snake"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    public void getAllPetTypesSuccessAsVetAdmin() throws Exception {
        petTypes.remove(0);
        petTypes.remove(1);
        given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get("/api/pettypes/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(2))
                .andExpect(jsonPath("$.[0].name").value("dog"))
                .andExpect(jsonPath("$.[1].id").value(4))
                .andExpect(jsonPath("$.[1].name").value("snake"));
    }

    @Test
    @WithMockUser(roles="VET_ADMIN")
    public void getAllPetTypesNotFound() throws Exception {
        petTypes.clear();
        given(this.clinicService.findAllPetTypes()).willReturn(petTypes);
        this.mockMvc.perform(get("/api/pettypes/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void updatePetTypeSuccess() throws Exception {
        given(this.clinicService.findPetTypeById(2)).willReturn(petTypes.get(1));
        PetType newPetType = petTypes.get(1);
        newPetType.setName("NEW DOG");
        ObjectMapper objectMapper = new ObjectMapper();
        String newPetTypeAsJson = objectMapper.writeValueAsString(newPetType);
        this.mockMvc.perform(put("/api/pettypes/2")
        .content(newPetTypeAsJson).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON))
        .andExpect(content().contentType("application/json"))
        .andDo(print())
        .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/api/pettypes/2")
        .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(2))
        .andExpect(jsonPath("$.name").value("NEW DOG"))
        .andDo(print());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void updatePetTypeError() throws Exception {
        PetType newPetType = petTypes.get(0);
        newPetType.setName("");
        ObjectMapper objectMapper = new ObjectMapper();
        String newPetTypeAsJson = objectMapper.writeValueAsString(newPetType);
        this.mockMvc.perform(put("/api/pettypes/1")
        .content(newPetTypeAsJson).accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void deletePetTypeSuccess() throws Exception {
        PetType newPetType = petTypes.get(0);
        ObjectMapper objectMapper = new ObjectMapper();
        String newPetTypeAsJson = objectMapper.writeValueAsString(newPetType);
        given(this.clinicService.findPetTypeById(1)).willReturn(petTypes.get(0));
        this.mockMvc.perform(delete("/api/pettypes/1")
        .contentType(newPetTypeAsJson).accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isNoContent());
    }

}
