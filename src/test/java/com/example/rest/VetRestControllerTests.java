package com.example.rest;

import com.example.exception.ExceptionControllerAdvice;
import com.example.model.entity.Vet;
import com.example.repository.VetRepository;
import com.example.service.ClinicService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
public class VetRestControllerTests {

    @Autowired
    private VetRestController vetRestController;
    @Autowired
    private VetRepository vetRepository;
    @MockBean
    private ClinicService clinicService;
    private MockMvc mockMvc;

    private List<Vet> vets;


    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(vetRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();

        vets = new ArrayList<Vet>();

        Vet vet = new Vet();
        vet.setId(1);
        vet.setFirstName("James");
        vet.setLastName("Carter");
        vets.add(vet);

        vet = new Vet();
        vet.setId(2);
        vet.setFirstName("Helen");
        vet.setLastName("Leary");
        vets.add(vet);

        vet = new Vet();
        vet.setId(3);
        vet.setFirstName("Linda");
        vet.setLastName("Douglas");
        vets.add(vet);
    }

    @AfterEach
    public void tearDown() {
        vetRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void addVetsSuccess() throws Exception {
        Vet newVet = vets.get(0);
        newVet.setId(999);
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsVet = objectMapper.writeValueAsString(newVet);
        this.mockMvc.perform(post("/api/vets")
                .content(writeValueAsVet)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void addVetsError() throws Exception {
        Vet nullVet = vets.get(0);
        nullVet.setId(null);
        nullVet.setFirstName(null);
        nullVet.setLastName(null);
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsNullVet = objectMapper.writeValueAsString(nullVet);
        this.mockMvc.perform(post("/api/vets")
                .content(writeValueAsNullVet)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void getAllVetsSuccess() throws Exception {
        given(this.clinicService.getAllVets()).willReturn(vets);
        this.mockMvc.perform(get("/api/vets/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].firstName").value("James"))
                .andExpect(jsonPath("$.[1].id").value(2))
                .andExpect(jsonPath("$.[1].firstName").value("Helen"));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void getAllVetsError() throws Exception {
        vets.clear();
        given(this.clinicService.getAllVets()).willReturn(vets);
        this.mockMvc.perform(get("/api/vets/")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void getFindByVetIdSuccess() throws Exception {
        given(this.clinicService.findByVetId(1)).willReturn(vets.get(0));
        this.mockMvc.perform(get("/api/vets/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("James"));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void getFindByVetIdError() throws Exception {
        given(this.clinicService.findByVetId(-1)).willReturn(null);
        this.mockMvc.perform(get("/api/vets/-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void updateVets() throws Exception {
        given(this.clinicService.findByVetId(1)).willReturn(vets.get(0));
        Vet newVet = vets.get(0);
        newVet.setFirstName("test");
        newVet.setLastName("test2");
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsnewVet = objectMapper.writeValueAsString(newVet);

        this.mockMvc.perform(put("/api/vets/1")
        .content(writeValueAsnewVet)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(content().contentType("application/json"))
        .andExpect(status().isNoContent());

        this.mockMvc.perform(get("/api/vets/1")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json"))
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.firstName").value("James"))
        .andExpect(jsonPath("$.lastName").value("Carter"));
    }

    @Test
    @WithMockUser(roles = "VET_ADMIN")
    public void deleteVet() throws Exception {
        Vet vet = vets.get(0);
        ObjectMapper objectMapper = new ObjectMapper();
        String writeValueAsString = objectMapper.writeValueAsString(vet);
        given(this.clinicService.findByVetId(1)).willReturn(vets.get(0));
        this.mockMvc.perform(delete("/api/vets/1")
        .content(writeValueAsString)
        .accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isNoContent());
    }
}
