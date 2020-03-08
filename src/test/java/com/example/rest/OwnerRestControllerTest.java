package com.example.rest;

import com.example.exception.ExceptionControllerAdvice;
import com.example.model.entity.Owner;
import com.example.model.entity.Pet;
import com.example.model.entity.PetType;
import com.example.model.entity.Visit;
import com.example.repository.OwnerRepository;
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
import java.util.Date;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@WebAppConfiguration
public class OwnerRestControllerTest {

    @Autowired
    private OwnerRestController ownerRestController;
    @Autowired
    private OwnerRepository ownerRepository;

    @MockBean
    private ClinicService clinicService;

    private MockMvc mockMvc;
    private List<Owner> owners;

    @BeforeEach
    public void initOwners(){
        this.mockMvc = MockMvcBuilders.standaloneSetup(ownerRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .build();

        owners = new ArrayList<Owner>();

        Owner ownerWithPet = new Owner();
        ownerWithPet.setId(1);
        ownerWithPet.setFirstName("George");
        ownerWithPet.setLastName("Franklin");
        ownerWithPet.setAddress("110 W. Liberty St.");
        ownerWithPet.setCity("Madison");
        ownerWithPet.setTelephone("6085551023");
        ownerWithPet.addPet(getTestPetWithIdAndName(ownerWithPet, 1, "Rosy"));
        owners.add(ownerWithPet);

        Owner owner = new Owner();
        owner.setId(2);
        owner.setFirstName("Betty");
        owner.setLastName("Davis");
        owner.setAddress("638 Cardinal Ave.");
        owner.setCity("Sun Prairie");
        owner.setTelephone("6085551749");
        owners.add(owner);

        owner = new Owner();
        owner.setId(3);
        owner.setFirstName("Eduardo");
        owner.setLastName("Rodriquez");
        owner.setAddress("2693 Commerce St.");
        owner.setCity("McFarland");
        owner.setTelephone("6085558763");
        owners.add(owner);

        owner = new Owner();
        owner.setId(4);
        owner.setFirstName("Harold");
        owner.setLastName("Davis");
        owner.setAddress("563 Friendly St.");
        owner.setCity("Windsor");
        owner.setTelephone("6085553198");
        owners.add(owner);
    }

    @AfterEach
    public void tearDown() {
        ownerRepository.deleteAll();
    }

    private Pet getTestPetWithIdAndName(final Owner owner, final int id, final String name) {
        PetType petType = new PetType();
        petType.setId(2);
        petType.setName("dog");
        Pet pet = new Pet();
        pet.setId(id);
        pet.setName(name);
        pet.setBirthDate(new Date());
        pet.setOwner(owner);
        pet.setType(petType);
        pet.addVisit(getTestVisitForPet(pet, 1));
        return pet;
    }

    private Visit getTestVisitForPet(final Pet pet, final int id) {
        Visit visit = new Visit();
        visit.setId(id);
        visit.setPet(pet);
        visit.setDate(new Date());
        visit.setDescription("test" + id);
        return visit;
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void creatOwnerSuccess() throws Exception {
        Owner newOwner = owners.get(0);
        newOwner.setId(null);
        ObjectMapper mapper = new ObjectMapper();
        String newOwnerAsJSON = mapper.writeValueAsString(newOwner);
        this.mockMvc.perform(post("/api/owners/")
                .content(newOwnerAsJSON)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void getOwnerSuccess() throws Exception {
        given(this.clinicService.findOwnerById(1)).willReturn(owners.get(0));
        this.mockMvc.perform(get("/api/owners/1")
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("George"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void getOwnerNotFound() throws Exception {
        given(this.clinicService.findOwnerById(-1)).willReturn(null); // if -1 -> return null
        this.mockMvc.perform(get("/api/owners/-1")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void getAllOwnersSuccess() throws Exception {
        owners.remove(0);
        owners.remove(1);
        given(this.clinicService.findAllOwners()).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.[0].id").value(2))
                .andExpect(jsonPath("$.[0].firstName").value("Betty"))
                .andExpect(jsonPath("$.[1].id").value(4))
                .andExpect(jsonPath("$.[1].firstName").value("Harold"));
    }

    @Test
    @WithMockUser(roles="OWNER_ADMIN")
    public void getAllOwnersNotFound() throws Exception {
        owners.clear();
        given(this.clinicService.findAllOwners()).willReturn(owners);
        this.mockMvc.perform(get("/api/owners/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


}
