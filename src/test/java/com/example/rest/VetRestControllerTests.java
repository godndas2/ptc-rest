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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
public class VetRestControllerTests {

    @Autowired private VetRestController vetRestController;
    @Autowired private VetRepository vetRepository;
    @MockBean private ClinicService clinicService;
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
}
