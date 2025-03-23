package clickgame.click_game_project.services;

import java.time.LocalDate;

import clickgame.click_game_project.entities.Game;

public interface GameWebSocketService {
boolean isOutOfLimit(int x, int y);
    void game(LocalDate startGame);
    void limitOnClicks(int numClicks);
    int controlScore();
    boolean clickValidation();
    Game save(Game game);
    void delete(int id);
}
