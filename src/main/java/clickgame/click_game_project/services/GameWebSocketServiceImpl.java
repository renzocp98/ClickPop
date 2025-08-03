package clickgame.click_game_project.services;

import java.util.ArrayList;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Service
public class GameWebSocketServiceImpl implements GameWebSocketService{
       

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameRepository gameRepository;    
    @Autowired
    private UserRepository userRepository;

    private int score = 0;    
    private int width = 300;  
    private int height = 300; 
    private int npoint = 100;    
    private boolean goodPoint;

    private List<int[]> points = new ArrayList<>();

    @Override
    public PointsOnGame RandomPoints() {
        Random random = new Random();
        points.clear(); 

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
        goodPoint = compareGamePoint(x, y);

        if(!out && goodPoint){
            return true;
        }       
        return false;
    }

   @Override
   public boolean compareGamePoint(int x, int y) {
    int radiusSquared = 8 * 8;

    for (int i = 0; i < points.size(); i++) {
        int[] point = points.get(i);
        int dx = x - point[0];
        int dy = y - point[1];
        if (dx * dx + dy * dy <= radiusSquared) {
            points.remove(i); // Eliminamos el punto que fue alcanzado
            return true;
        }
    }

    return false;
}

    
    @Override
    public boolean isOutOfLimit(int x, int y) {
        if(x < 0 || x > 300 || y < 0 || y > 300){
            return true;
        } return false; 
    }


    @Override
    public int controlScore(int x, int y) {
        
        if(goodPoint) {
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


    @Override
    public Game createGame(User user) {
       User existUser = userRepository.findById(user.getId())
           .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
   
       Game game = new Game();
       game.setUser(existUser);
       game.setScore(0);
       gameRepository.save(game);
   
       PointsOnGame pointsOnGame = RandomPoints();
   
       messagingTemplate.convertAndSend("/backsend/points", pointsOnGame);
   
       return game;
    }

    @Override
    public Game findById(int id) {

        Optional<Game> gameExist = gameRepository.findById(id);
        if(gameExist.isPresent()){
            Game actualGame = gameExist.orElseThrow();
            return actualGame;
        }
        return null;
    }



    @Override
    public void AddScore(Game game, int score) {
        game.setScore(score);
        gameRepository.save(game);
        
    }


   

}
