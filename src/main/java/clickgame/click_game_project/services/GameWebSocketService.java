package clickgame.click_game_project.services;


import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.models.PointsOnGame;

public interface GameWebSocketService {
    boolean isOutOfLimit(int x, int y);
    int controlScore(int x, int y);
    boolean clickValidation(int x, int y);
    Game save(Game game);
    void delete(int id);
    PointsOnGame RandomPoints();
    Game createGame(User user);
    boolean compareGamePoint(int x, int y);
}
