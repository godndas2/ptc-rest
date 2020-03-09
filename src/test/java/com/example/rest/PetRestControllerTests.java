package com.example.rest;

import com.example.exception.ExceptionControllerAdvice;
import com.example.model.entity.Owner;
import com.example.model.entity.Pet;
import com.example.model.entity.PetType;
import com.example.service.ClinicService;
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
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class PetRestControllerTests {

    @Autowired
    private PetRestController petRestController;
    @MockBean
    protected ClinicService clinicService;

    private MockMvc mockMvc;

    private List<Pet> pets;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(petRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();

        pets = new ArrayList<>();

        Owner owner = new Owner();
        owner.setId(1);
        owner.setFirstName("Eduardo");
        owner.setLastName("Rodriquez");
        owner.setAddress("2693 Commerce St.");
        owner.setCity("McFarland");
        owner.setTelephone("6085558763");

        PetType petType = new PetType();
        petType.setId(2);
        petType.setName("dog");

        Pet pet = new Pet();
        pet.setId(3);
        pet.setName("Rosy");
        pet.setBirthDate(new Date());
        pet.setOwner(owner);
        pet.setType(petType);
        pets.add(pet);

        pet = new Pet();
        pet.setId(4);
        pet.setName("Jewel");
        pet.setBirthDate(new Date());
        pet.setOwner(owner);
        pet.setType(petType);
        pets.add(pet);
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    public void getPetSuccess() throws Exception {
        given(this.clinicService.findPetById(3)).willReturn(pets.get(0));
        this.mockMvc.perform(get("/api/pets/3")
        .accept(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Rosy"));
    }

    @Test
    @WithMockUser(roles = "OWNER_ADMIN")
    public void getPetNotFound() throws Exception {
        given(this.clinicService.findPetById(-1)).willReturn(null);
        this.mockMvc.perform(get("/api/pets/-1")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
    }

}
