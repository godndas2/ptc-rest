package com.example.rest;

import com.example.exception.ExceptionControllerAdvice;
import com.example.model.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@WebAppConfiguration
@TestConfiguration
public class UserRestControllerTests {

    @Mock
    private UserService userService;

    @Autowired
    private UserRestController userRestController;
    @Autowired
    private UserRepository userRepository;

    private MockMvc mockMvc;

    public UserRestControllerTests() {
        MockitoAnnotations.initMocks(this);
    }

    @BeforeEach
    public void initVets() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
                .setControllerAdvice(new ExceptionControllerAdvice())
                .addFilter(new CharacterEncodingFilter("UTF-8"))
                .build();
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void createUser() throws Exception {
        User user = new User();
        user.setUsername("userName");
        user.setPassword("1234");
        user.setEnabled(true);
        user.addRole("OWNER_ADMIN");

        ObjectMapper objectMapper = new ObjectMapper();
        String newVetAsJSON = objectMapper.writeValueAsString(user);
        this.mockMvc.perform(post("/api/users/")
        .content(newVetAsJSON)
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isCreated());
    }

}
