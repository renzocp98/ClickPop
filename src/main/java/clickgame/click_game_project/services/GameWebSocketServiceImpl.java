package clickgame.click_game_project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.models.PointsOnGame;
import clickgame.click_game_project.repositories.GameRepository;
import clickgame.click_game_project.repositories.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class GameWebSocketServiceImpl implements GameWebSocketService {

    private static final int BOARD_WIDTH  = 300;
    private static final int BOARD_HEIGHT = 300;
    private static final int NUM_POINTS   = 15;
    private static final int CLICK_RADIUS = 15;
    private static final int MISS_PENALTY = 2;

    // Estado aislado por partida: cada gameId tiene su propio score y sus propios puntos
    private final Map<Integer, Integer>       scoreByGame  = new ConcurrentHashMap<>();
    private final Map<Integer, List<int[]>>   pointsByGame = new ConcurrentHashMap<>();

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public PointsOnGame RandomPoints(int gameId) {
        Random random = new Random();
        List<int[]> points = new ArrayList<>();

        for (int i = 0; i < NUM_POINTS; i++) {
            int x = random.nextInt(BOARD_WIDTH);
            int y = random.nextInt(BOARD_HEIGHT);
            points.add(new int[]{x, y});
        }

        pointsByGame.put(gameId, points);
        scoreByGame.put(gameId, 0);

        return new PointsOnGame(points);
    }


    @Override
    public boolean isOutOfLimit(int x, int y) {
        return x < 0 || x > BOARD_WIDTH || y < 0 || y > BOARD_HEIGHT;
    }


    @Override
    public boolean compareGamePoint(int gameId, int x, int y) {
        List<int[]> points = pointsByGame.get(gameId);
        if (points == null) return false;

        int radiusSquared = CLICK_RADIUS * CLICK_RADIUS;

        for (int i = 0; i < points.size(); i++) {
            int[] point = points.get(i);
            int dx = x - point[0];
            int dy = y - point[1];
            if (dx * dx + dy * dy <= radiusSquared) {
                points.remove(i);
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean isGameFinished(int gameId) {
        List<int[]> points = pointsByGame.get(gameId);
        return points != null && points.isEmpty();
    }


    @Override
    public boolean clickValidation(int gameId, int x, int y) {
        if (isOutOfLimit(x, y)) return false;
        return compareGamePoint(gameId, x, y);
    }


    @Override
    public int controlScore(int gameId, boolean isValid) {
        // Si el juego no está registrado en memoria no penalizamos:
        // puede que el gameId sea incorrecto o la partida aún no se haya inicializado.
        if (!scoreByGame.containsKey(gameId)) {
            return 0;
        }
        int current = scoreByGame.get(gameId);
        int updated = isValid ? current + 1 : current - MISS_PENALTY;
        scoreByGame.put(gameId, updated);
        return updated;
    }


    @Override
    public Game createGame(User user) {
        User existUser = userRepository.findById(user.getId())  
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Game game = new Game();
        game.setUser(existUser);
        game.setScore(0);
        gameRepository.save(game);

        PointsOnGame pointsOnGame = RandomPoints(game.getId());

        messagingTemplate.convertAndSend("/backsend/points", pointsOnGame);

        return game;
    }


    @Override
    public void AddScore(Game game, int score) {
        game.setScore(score);
        gameRepository.save(game);
        // Limpiamos el estado en memoria una vez finalizada la partida
        scoreByGame.remove(game.getId());
        pointsByGame.remove(game.getId());
    }


    @Override
    public Optional<Game> findById(int id) {
        return gameRepository.findById(id);
    }


    @Transactional
    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }


    @Transactional
    @Override
    public void delete(int id) {
        Optional<Game> gameOptional = gameRepository.findById(id);
        gameOptional.ifPresent(gameRepository::delete);
    }

}
