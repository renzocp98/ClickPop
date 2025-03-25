package clickgame.click_game_project.services;


import clickgame.click_game_project.entities.Game;

public interface GameWebSocketService {
boolean isOutOfLimit(int x, int y);
    int controlScore();
    boolean clickValidation(int x, int y);
    Game save(Game game);
    void delete(int id);
}
