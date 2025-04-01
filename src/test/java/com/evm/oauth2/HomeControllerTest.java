package com.evm.oauth2;

import com.evm.oauth2.domain.models.Role;
import com.evm.oauth2.domain.models.User;
import com.evm.oauth2.domain.ports.out.UserRepositoryPort;
import com.evm.oauth2.infrastructure.dto.request.CredentialsRequest;
import com.evm.oauth2.infrastructure.dto.response.AccessJwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
public class HomeControllerTest extends AbstractIntegrationTest {

    private static final String testMail = "test@example.com";
    private static final String testUsername = "testUser";
    private static final String testPassword = "password";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void beforeEach() {
        userRepositoryPort.deleteAll();
    }

    @Test
    @Order(10)
    public void get_shouldReturnUnauthorized_tokenIsInvalid() throws Exception {
        mockMvc.perform(get("/api/v1/home")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Order(20)
    public void get_shouldReturnOk() throws Exception {
        User user = createUser();
        userRepositoryPort.saveUser(user);

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

        mockMvc.perform(get("/api/v1/home")
                        .header("Authorization", "Bearer ".concat(jwtResponse.getJwtToken())))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @Order(30)
    public void getAdmin_shouldReturnForbidden_userDoesNotHaveADMIN() throws Exception {
        User user = createUser();
        userRepositoryPort.saveUser(user);

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

        mockMvc.perform(get("/api/v1/home/admin")
                        .header("Authorization", "Bearer ".concat(jwtResponse.getJwtToken())))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @Order(40)
    public void getAdmin_shouldReturnOk_userHasADMIN() throws Exception {
        Role roleUser = new Role();
        roleUser.setRole("ROLE_USER");
        Role roleAdmin = new Role();
        roleAdmin.setRole("ROLE_ADMIN");

        User user = createUser();
        user.setRoles(List.of(roleUser, roleAdmin));
        userRepositoryPort.saveUser(user);

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

        mockMvc.perform(get("/api/v1/home/admin")
                        .header("Authorization", "Bearer ".concat(jwtResponse.getJwtToken())))
                .andExpect(status().isOk())
                .andReturn();
    }

    private User createUser() {
        var user = new User();
        user.setEmail(testMail);
        user.setUsername(testUsername);
        user.setPassword(passwordEncoder.encode(testPassword));
        user.setLoginIssuer("main-app");
        user.setAccountEnabled(true);
        user.setAccountExpired(false);
        user.setAccountLocked(false);
        user.setCredentialsExpired(false);

        var role = new Role();
        role.setRole("ROLE_USER");
        role.setUser(user);

        user.setRoles(List.of(role));

        return user;
    }

}
