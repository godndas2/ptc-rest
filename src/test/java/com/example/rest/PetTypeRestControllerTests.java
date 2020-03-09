package com.example.rest;

import com.example.exception.ExceptionControllerAdvice;
import com.example.model.entity.PetType;
import com.example.repository.PetTypeRepository;
import com.example.service.ClinicService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

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

//    @Test
//    @WithMockUser(roles = "OWNER_ADMIN")
//    public void getPetTypeSuccessAsOwnerAdmin() throws Exception {
//        given(this.clinicService.findPetById(1)).willReturn(petTypes.get(0));
//    }
}
