package clickgame.click_game_project.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.services.GameWebSocketService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class GameControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameWebSocketService gameWebSocketService;

    private User user;
    private Game game;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1);
        user.setUsername("Renzo");

        game = new Game();
        game.setId(10);
        game.setUser(user);
        game.setScore(0);
    }

    // ─── POST /game/create ────────────────────────────────────────────────────

    @Test
    void testCreateGame_returnsCreated_whenUserExists() throws Exception {
        when(gameWebSocketService.createGame(any(User.class))).thenReturn(game);

        mockMvc.perform(post("/game/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 1}"))
            .andExpect(status().isCreated());
    }

    @Test
    void testCreateGame_returnsBadRequest_whenUserNotFound() throws Exception {
        when(gameWebSocketService.createGame(any(User.class)))
            .thenThrow(new RuntimeException("Usuario no encontrado"));

        mockMvc.perform(post("/game/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 99}"))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Usuario no encontrado"));
    }

    // ─── PUT /game/score/{score} ──────────────────────────────────────────────

    @Test
    void testAddScore_returnsOk_whenGameExists() throws Exception {
        when(gameWebSocketService.findById(10)).thenReturn(Optional.of(game));
        doNothing().when(gameWebSocketService).AddScore(any(Game.class), anyInt());

        mockMvc.perform(put("/game/score/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 10}"))
            .andExpect(status().isOk());
    }

    @Test
    void testAddScore_returnsNotFound_whenGameDoesNotExist() throws Exception {
        when(gameWebSocketService.findById(anyInt())).thenReturn(Optional.empty());

        mockMvc.perform(put("/game/score/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 999}"))
            .andExpect(status().isNotFound());
    }
}
