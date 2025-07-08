package clickgame.click_game_project.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.models.PointsOnGame;
import clickgame.click_game_project.repositories.GameRepository;
import clickgame.click_game_project.repositories.UserRepository;

@Service
public class GameWebSocketServiceImpl implements GameWebSocketService{
       

    @Autowired
    private GameRepository gameRepository;    
    @Autowired
    private UserRepository userRepository;

    private int score = 0;    
    private int width = 200;  
    private int height = 200; 
    private int npoint = 100;    

    private List<int[]> points = new ArrayList<>();

    @Override
    public PointsOnGame RandomPoints() {


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
        
        boolean out = isOutOfLimit(x, y);
        boolean goodPoint = compareGamePoint(x, y);

        if(!out && goodPoint){
            return true;
        }       
        return false;
    }

    public boolean compareGamePoint(int x, int y){
        int[] pointsOnGame = {x,y};
        return points.stream().anyMatch(point -> Arrays.equals(point, pointsOnGame));
    }

    
    @Override
    public boolean isOutOfLimit(int x, int y) {
        if(x < 0 || x > 200 || y < 0 || y > 200){
            return true;
        } return false; 
    }


    @Override
    public int controlScore(int x, int y) {
        
        if(compareGamePoint(x, y)) {
            return score++;
        }
        return score = score - 2;

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


    public Game createGame(User user) {
    
        User existUser = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
        Game game = new Game();
        game.setUser(existUser);
        game.setScore(0);
    
        return gameRepository.save(game);
    }

   

}
