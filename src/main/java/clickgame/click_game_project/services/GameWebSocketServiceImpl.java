package clickgame.click_game_project.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.repositories.GameRepository;

public class GameWebSocketServiceImpl implements GameWebSocketService{
    
    private int score;
    

    @Autowired
    private GameRepository gameRepository;    


    @Override
    public boolean clickValidation(int x, int y) {
        
        isOutOfLimit(x, y);

        
        return false;
    }



    @Override
    public boolean isOutOfLimit(int x, int y) {
        if(x < 0 || x > 200 || y < 0 || y > 200){
            return true;
        } return false; 
    }


    @Override
    public int controlScore() {

        return 0;
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
