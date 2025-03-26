package clickgame.click_game_project.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.models.PointsOnGame;
import clickgame.click_game_project.repositories.GameRepository;

public class GameWebSocketServiceImpl implements GameWebSocketService{
       

    @Autowired
    private GameRepository gameRepository;    


    private int width = 200;  
    private int height = 200; 
    private int npoint = 100;    

    @Override
    public PointsOnGame RandomPoints() {

        List<int[]> points = new ArrayList<>();

        Random random = new Random();

        for (int i = 0; i < npoint; i++) {
            
            int x = random.nextInt(width);   
            int y = random.nextInt(height);  
            points.add(new int[]{x, y});
        }

        return new PointsOnGame(points);
    }


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
