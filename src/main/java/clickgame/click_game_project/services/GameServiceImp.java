package clickgame.click_game_project.services;

import org.springframework.beans.factory.annotation.Autowired;

import clickgame.click_game_project.repositories.GameRepository;
import clickgame.click_game_project.repositories.UserRepository;

public class GameServiceImp implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

}
