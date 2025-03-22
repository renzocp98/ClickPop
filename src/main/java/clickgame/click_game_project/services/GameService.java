package clickgame.click_game_project.services;

import java.time.LocalDate;

import clickgame.click_game_project.entities.Game;

public interface GameService {
    boolean isOutOfLimit();
    void game(LocalDate startGame);
    void limitOnClicks();
    void controlScore();
    Game save(Game game);
    void delete(int id);

}
