package clickgame.click_game_project.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.Role;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.models.PointsOnGame;
import clickgame.click_game_project.repositories.GameRepository;
import clickgame.click_game_project.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class GameWebSocketServiceImplTests {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private GameWebSocketServiceImpl gameService;

    private static final int GAME_ID = 1;

    private User user;
    private Game game;

    @BeforeEach
    void setup() {
        Role role = new Role();
        role.setName("USER");

        user = new User();
        user.setId(1);
        user.setUsername("Renzo");
        user.setRole(role);

        game = new Game();
        game.setId(GAME_ID);
        game.setUser(user);
        game.setScore(0);
    }

    // ─── RandomPoints ─────────────────────────────────────────────────────────

    @Test
    void testRandomPoints_generates15Points() {
        PointsOnGame result = gameService.RandomPoints(GAME_ID);
        assertThat(result.getPoints()).hasSize(15);
    }

    @Test
    void testRandomPoints_allPointsWithinBoardBounds() {
        PointsOnGame result = gameService.RandomPoints(GAME_ID);
        for (int[] point : result.getPoints()) {
            assertThat(point[0]).isBetween(0, 299);
            assertThat(point[1]).isBetween(0, 299);
        }
    }

    @Test
    void testRandomPoints_resetsScoreToZero() {
        gameService.RandomPoints(GAME_ID);
        gameService.controlScore(GAME_ID, false); // -2
        gameService.controlScore(GAME_ID, false); // -4

        gameService.RandomPoints(GAME_ID); // reset: score vuelve a 0

        // Primer click válido tras reset debe ser 1, no -3
        assertThat(gameService.controlScore(GAME_ID, true)).isEqualTo(1);
    }

    // ─── isOutOfLimit ─────────────────────────────────────────────────────────

    @Test
    void testIsOutOfLimit_returnsFalse_whenInsideBounds() {
        assertThat(gameService.isOutOfLimit(0, 0)).isFalse();
        assertThat(gameService.isOutOfLimit(150, 150)).isFalse();
        assertThat(gameService.isOutOfLimit(300, 300)).isFalse();
    }

    @Test
    void testIsOutOfLimit_returnsTrue_whenXNegative() {
        assertThat(gameService.isOutOfLimit(-1, 150)).isTrue();
    }

    @Test
    void testIsOutOfLimit_returnsTrue_whenYNegative() {
        assertThat(gameService.isOutOfLimit(150, -1)).isTrue();
    }

    @Test
    void testIsOutOfLimit_returnsTrue_whenXExceedsBoard() {
        assertThat(gameService.isOutOfLimit(301, 0)).isTrue();
    }

    @Test
    void testIsOutOfLimit_returnsTrue_whenYExceedsBoard() {
        assertThat(gameService.isOutOfLimit(0, 301)).isTrue();
    }

    // ─── compareGamePoint ─────────────────────────────────────────────────────

    @Test
    void testCompareGamePoint_returnsFalse_whenGameNotRegistered() {
        assertThat(gameService.compareGamePoint(999, 100, 100)).isFalse();
    }

    @Test
    void testCompareGamePoint_returnsTrue_whenClickHitsPoint() {
        PointsOnGame pointsOnGame = gameService.RandomPoints(GAME_ID);
        int[] firstPoint = pointsOnGame.getPoints().get(0);

        assertThat(gameService.compareGamePoint(GAME_ID, firstPoint[0], firstPoint[1])).isTrue();
    }

    @Test
    void testCompareGamePoint_removesHitPoint_soSecondHitMisses() {
        PointsOnGame pointsOnGame = gameService.RandomPoints(GAME_ID);
        int[] firstPoint = pointsOnGame.getPoints().get(0);

        gameService.compareGamePoint(GAME_ID, firstPoint[0], firstPoint[1]); // acierto → se elimina

        // Las mismas coordenadas ya no deben acertar (el punto fue eliminado)
        assertThat(gameService.compareGamePoint(GAME_ID, firstPoint[0], firstPoint[1])).isFalse();
    }

    // ─── controlScore ─────────────────────────────────────────────────────────

    @Test
    void testControlScore_returnsZero_whenGameNotRegistered() {
        assertThat(gameService.controlScore(999, true)).isEqualTo(0);
    }

    @Test
    void testControlScore_incrementsByOne_whenValid() {
        gameService.RandomPoints(GAME_ID);
        assertThat(gameService.controlScore(GAME_ID, true)).isEqualTo(1);
    }

    @Test
    void testControlScore_decrementsByTwo_whenInvalid() {
        gameService.RandomPoints(GAME_ID);
        assertThat(gameService.controlScore(GAME_ID, false)).isEqualTo(-2);
    }

    @Test
    void testControlScore_accumulatesCorrectly() {
        gameService.RandomPoints(GAME_ID);
        gameService.controlScore(GAME_ID, true);  // 1
        gameService.controlScore(GAME_ID, true);  // 2
        gameService.controlScore(GAME_ID, false); // 0
        int score = gameService.controlScore(GAME_ID, true); // 1
        assertThat(score).isEqualTo(1);
    }

    // ─── isGameFinished ───────────────────────────────────────────────────────

    @Test
    void testIsGameFinished_returnsFalse_whenGameNotRegistered() {
        assertThat(gameService.isGameFinished(999)).isFalse();
    }

    @Test
    void testIsGameFinished_returnsFalse_whenPointsRemain() {
        gameService.RandomPoints(GAME_ID);
        assertThat(gameService.isGameFinished(GAME_ID)).isFalse();
    }

    @Test
    void testIsGameFinished_returnsTrue_whenAllPointsHit() {
        PointsOnGame pointsOnGame = gameService.RandomPoints(GAME_ID);
        // Snapshot para no iterar sobre la lista mientras compareGamePoint la modifica
        List<int[]> snapshot = new ArrayList<>(pointsOnGame.getPoints());

        for (int[] point : snapshot) {
            gameService.compareGamePoint(GAME_ID, point[0], point[1]);
        }

        assertThat(gameService.isGameFinished(GAME_ID)).isTrue();
    }

    // ─── clickValidation ──────────────────────────────────────────────────────

    @Test
    void testClickValidation_returnsFalse_whenOutOfBounds() {
        gameService.RandomPoints(GAME_ID);
        assertThat(gameService.clickValidation(GAME_ID, -1, 150)).isFalse();
        assertThat(gameService.clickValidation(GAME_ID, 400, 150)).isFalse();
    }

    @Test
    void testClickValidation_returnsTrue_whenHitsPoint() {
        PointsOnGame pointsOnGame = gameService.RandomPoints(GAME_ID);
        int[] firstPoint = pointsOnGame.getPoints().get(0);

        assertThat(gameService.clickValidation(GAME_ID, firstPoint[0], firstPoint[1])).isTrue();
    }

    // ─── AddScore ─────────────────────────────────────────────────────────────

    @Test
    void testAddScore_savesScoreToDb() {
        gameService.RandomPoints(GAME_ID);

        gameService.AddScore(game, 7);

        assertThat(game.getScore()).isEqualTo(7);
        verify(gameRepository).save(game);
    }

    @Test
    void testAddScore_cleansUpInMemoryState() {
        gameService.RandomPoints(GAME_ID);
        gameService.AddScore(game, 5);

        // gameId ya no está registrado: controlScore devuelve 0, isGameFinished devuelve false
        assertThat(gameService.controlScore(GAME_ID, true)).isEqualTo(0);
        assertThat(gameService.isGameFinished(GAME_ID)).isFalse();
    }

    // ─── createGame ───────────────────────────────────────────────────────────

    @Test
    void testCreateGame_returnsGameWithScoreZero() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(gameRepository.save(any(Game.class))).thenAnswer(inv -> {
            Game g = inv.getArgument(0);
            g.setId(10);
            return g;
        });

        Game created = gameService.createGame(user);

        assertThat(created).isNotNull();
        assertThat(created.getScore()).isEqualTo(0);
    }

    @Test
    void testCreateGame_sendsPointsViaWebSocket() {
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(gameRepository.save(any(Game.class))).thenAnswer(inv -> {
            Game g = inv.getArgument(0);
            g.setId(10);
            return g;
        });

        gameService.createGame(user);

        verify(messagingTemplate).convertAndSend(anyString(), any(PointsOnGame.class));
    }

    @Test
    void testCreateGame_throwsWhenUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> gameService.createGame(user))
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Usuario no encontrado");
    }
}
