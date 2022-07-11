package ru.lappi.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.lappi.users.controller.UserController;
import ru.lappi.users.entity.User;
import ru.lappi.users.entity.projection.ObjectId;
import ru.lappi.users.repository.UserRepository;
import ru.lappi.users.service.TestObjectsCreator;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Nikita Gorodilov
 */
@AutoConfigureMockMvc
public class UserControllerTest extends AbstractTest {
    public static final String USER_CONTROLLER_URL = "/users";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserController controller;
    @Autowired
    private TestObjectsCreator testObjectsCreator;

    private final static String USERNAME = UUID.randomUUID().toString();
    private final static String PASSWORD = UUID.randomUUID().toString();

    @BeforeEach
    void init() {
        testObjectsCreator.createRandomUserWithRoles(USERNAME, PASSWORD);
    }

    @Test
    void testLoginSuccessful() throws Exception {
        mockMvc.perform(
                post(USER_CONTROLLER_URL + "/login")
                        .param("username", USERNAME)
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    void testLoginWrongUsername() throws Exception {
        mockMvc.perform(
                post(USER_CONTROLLER_URL + "/login")
                        .param("username", UUID.randomUUID().toString())
                        .param("password", PASSWORD)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("false")));
    }

    @Test
    void testLoginWrongPassword() throws Exception {
        mockMvc.perform(
                post(USER_CONTROLLER_URL + "/login")
                        .param("username", USERNAME)
                        .param("password", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("false")));
    }

    @Test
    void testRegisterSuccessful() throws Exception {
        mockMvc.perform(
                post(USER_CONTROLLER_URL + "/register")
                        .param("username", UUID.randomUUID().toString())
                        .param("password", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(containsString("true")));
    }

    @Test
    void testRegisterSameUsername() throws Exception {
        mockMvc.perform(
                post(USER_CONTROLLER_URL + "/register")
                        .param("username", USERNAME)
                        .param("password", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        )
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("false")));
    }

    @Test
    void testGetUserIdSuccessful() throws Exception {
        String result = mockMvc.perform(
                post(USER_CONTROLLER_URL + "/getUserId")
                        .param("username", USERNAME)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(result);
        assertDoesNotThrow(() -> Long.valueOf(result));
    }

    @Test
    void testGetUserIdUserNotFound() throws Exception {
        mockMvc.perform(
                post(USER_CONTROLLER_URL + "/getUserId")
                        .param("username", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetUserDataSuccessful() throws Exception {
        String result = mockMvc.perform(
                post(USER_CONTROLLER_URL + "/getUserData")
                        .param("username", USERNAME)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(result);
        assertTrue(result.contains("userId"));
        assertTrue(result.contains("privilegeCodes"));

        User user = userRepository.findByUsername(USERNAME)
                .orElseThrow(RuntimeException::new);
        assertTrue(user.getAllPrivilegeCodes().size() > 0);
        user.getAllPrivilegeCodes().forEach(it -> assertTrue(result.contains(it)));
    }

    @Test
    void testGetUserDataNotFound() throws Exception {
        mockMvc.perform(
                post(USER_CONTROLLER_URL + "/getUserData")
                        .param("username", UUID.randomUUID().toString())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
        )
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetUserDataByUserIdSuccessful() throws Exception {
        Optional<ObjectId> userId = userRepository.findUserIdByUsername(USERNAME);
        assertTrue(userId.isPresent());
        Long id = userId.get().getId();
        assertNotNull(id);

        String result = mockMvc.perform(
                        post(USER_CONTROLLER_URL + "/getUserDataByUserId")
                                .param("user_id", id.toString())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        assertNotNull(result);
        assertTrue(result.contains("userId"));
        assertTrue(result.contains("privilegeCodes"));

        User user = userRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        assertTrue(user.getAllPrivilegeCodes().size() > 0);
        user.getAllPrivilegeCodes().forEach(it -> assertTrue(result.contains(it)));
    }

    @Test
    void testGetUserDataByUserIdNotFound() throws Exception {
        mockMvc.perform(
                        post(USER_CONTROLLER_URL + "/getUserDataByUserId")
                                .param("user_id", Long.valueOf(-1L).toString())
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                                .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is4xxClientError());
    }
}
