package clickgame.click_game_project.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.repositories.GameRepository;
import clickgame.click_game_project.repositories.UserRepository;

@Service
public class GameServiceImp implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Override
    public void game(LocalDate startGame) {
        

        
    }

    private void clickCalculation(){
        
    }


    @Override
    public boolean isOutOfLimit() {
        
        return false;
    }

    

    @Override
    public void limitOnClicks() {
        
        
    }

    @Override
    public void controlScore() {
        
        
    }

    @Transactional
    @Override
    public void delete(int id) {
        
        Optional<Game> gameOptional = gameRepository.findById(id);
        gameOptional.ifPresent(gameRepository::delete);
    }

    @Transactional
    @Override
    public Game save(Game game) {
        return gameRepository.save(game);
    }


    

    

}
