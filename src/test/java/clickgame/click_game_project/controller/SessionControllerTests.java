package clickgame.click_game_project.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import clickgame.click_game_project.entities.Role;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.services.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User existingUser;

    @BeforeEach
    void setup() {
        Role role = new Role();
        role.setName("USER");

        existingUser = new User();
        existingUser.setId(1);
        existingUser.setUsername("Renzo");
        existingUser.setPassword("12345");
        existingUser.setRole(role);
    }

    // ─── POST /SessionInfo/login ──────────────────────────────────────────────

    @Test
    void testLogin_returnsOk_whenValidCredentials() throws Exception {
        when(userService.findByUsername("Renzo")).thenReturn(existingUser);

        mockMvc.perform(post("/SessionInfo/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"Renzo\", \"password\": \"12345\"}"))
            .andExpect(status().isOk());
    }

    @Test
    void testLogin_returnsUnauthorized_whenWrongPassword() throws Exception {
        when(userService.findByUsername("Renzo")).thenReturn(existingUser);

        mockMvc.perform(post("/SessionInfo/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"Renzo\", \"password\": \"wrongpass\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Usuario/contraseña incorrecta"));
    }

    @Test
    void testLogin_returns500_whenUserNotFound() throws Exception {
        // findByUsername lanza RuntimeException si el usuario no existe (ver UserServiceImpl).
        // SessionController no captura esa excepción → Spring devuelve 500.
        // Este test documenta el comportamiento real; idealmente debería ser 401.
        when(userService.findByUsername("unknown"))
            .thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(post("/SessionInfo/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"unknown\", \"password\": \"12345\"}"))
            .andExpect(status().isInternalServerError());
    }

    // ─── POST /SessionInfo/logout ─────────────────────────────────────────────

    @Test
    void testLogout_returnsOk() throws Exception {
        mockMvc.perform(post("/SessionInfo/logout"))
            .andExpect(status().isOk())
            .andExpect(content().string("Sesión cerrada correctamente"));
    }
}
