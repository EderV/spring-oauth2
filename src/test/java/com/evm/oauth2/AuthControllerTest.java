package com.evm.oauth2;

import com.evm.oauth2.domain.ports.out.UserRepositoryPort;
import com.evm.oauth2.infrastructure.dto.request.CredentialsRequest;
import com.evm.oauth2.infrastructure.dto.request.RegistrationRequest;
import com.evm.oauth2.infrastructure.dto.response.AccessJwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class AuthControllerTest extends AbstractIntegrationTest {

    private static final String testMail = "test@example.com";
    private static final String testUsername = "testUser";
    private static final String testPassword = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Test
    @Order(10)
    public void registrationEndpoint_shouldReturnCreated() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(testMail);
        request.setUsername(testUsername);
        request.setPassword(testPassword);

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        var savedUser = userRepositoryPort.getUserFromUsername("testUser");
        assertNotNull(savedUser);
    }

    @Test
    @Order(15)
    public void registrationEndpoint_shouldReturnConflict_UserAlreadyRegistered() throws Exception {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail(testMail);
        request.setUsername(testUsername);
        request.setPassword(testPassword);

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isConflict());
    }

    @Test
    @Order(20)
    public void loginEndpoint_shouldReturnOk() throws Exception {
        CredentialsRequest request = new CredentialsRequest();
        request.setUsername(testUsername);
        request.setPassword(testPassword);

        String jsonRequest = objectMapper.writeValueAsString(request);

        MvcResult result = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = result.getResponse().getContentAsString();
        AccessJwtResponse jwtResponse = objectMapper.readValue(responseString, AccessJwtResponse.class);

        assertNotNull(jwtResponse);
        assertTrue(jwtResponse.getUserId() > 0, "User ID should be greater than 0");
        assertNotNull(jwtResponse.getJwtToken(), "JWT token should not be null");
        assertFalse(jwtResponse.getJwtToken().isEmpty(), "JWT token should not be empty");
    }

    @Test
    @Order(30)
    public void loginEndpoint_shouldReturnUnauthorized_passwordIncorrect() throws Exception {
        CredentialsRequest request = new CredentialsRequest();
        request.setUsername(testUsername);
        request.setPassword("1234");

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @Order(40)
    public void loginEndpoint_shouldReturnUnauthorized_userNotFound() throws Exception {
        CredentialsRequest request = new CredentialsRequest();
        request.setUsername("user-not-in-db");
        request.setPassword("1234");

        String jsonRequest = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUnauthorized());
    }

}

