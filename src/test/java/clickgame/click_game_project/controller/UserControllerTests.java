package clickgame.click_game_project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import clickgame.click_game_project.entities.Role;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User savedUser;

    @BeforeEach
    void setup() {
        Role role = new Role();
        role.setId(1);
        role.setName("USER");

        savedUser = new User();
        savedUser.setId(1);
        savedUser.setUsername("Renzo");
        savedUser.setPassword("12345");
        savedUser.setCountry("Spain");
        savedUser.setRole(role);
    }

    // ─── POST /users/register ─────────────────────────────────────────────────

    @Test
    void testRegister_returnsCreated_whenValidUser() throws Exception {
        when(userService.save(any(User.class))).thenReturn(savedUser);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username": "Renzo", "password": "12345", "country": "Spain"}
                        """))
            .andExpect(status().isCreated());
    }

    @Test
    void testRegister_returnsBadRequest_whenUsernameIsBlank() throws Exception {
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username": "", "password": "12345"}
                        """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testRegister_returnsBadRequest_whenPasswordIsBlank() throws Exception {
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username": "Renzo", "password": ""}
                        """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testRegister_returnsBadRequest_whenPasswordTooShort() throws Exception {
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"username": "Renzo", "password": "123"}
                        """))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$[0]").value("password: La contraseña debe tener al menos 4 caracteres"));
    }

    // ─── GET /users ───────────────────────────────────────────────────────────

    @Test
    void testList_returnsOk_andUserList() throws Exception {
        when(userService.findAll()).thenReturn(List.of(savedUser));

        mockMvc.perform(get("/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].username").value("Renzo"));
    }

    // ─── GET /users/username/{username} ──────────────────────────────────────

    @Test
    void testGetUser_returnsOk_whenUserExists() throws Exception {
        when(userService.findByUsername("Renzo")).thenReturn(savedUser);

        mockMvc.perform(get("/users/username/Renzo"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("Renzo"));
    }
}
