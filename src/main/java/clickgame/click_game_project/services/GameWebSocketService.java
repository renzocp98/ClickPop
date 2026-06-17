package clickgame.click_game_project.services;

import java.util.Optional;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.models.PointsOnGame;

public interface GameWebSocketService {
    boolean isOutOfLimit(int x, int y);
    boolean clickValidation(int gameId, int x, int y);
    boolean compareGamePoint(int gameId, int x, int y);
    boolean isGameFinished(int gameId);
    int controlScore(int gameId, boolean isValid);
    PointsOnGame RandomPoints(int gameId);
    Game createGame(User user);
    Game save(Game game);
    Optional<Game> findById(int id);
    void AddScore(Game game, int score);
    void delete(int id);
}
