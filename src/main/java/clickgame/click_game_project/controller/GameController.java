package clickgame.click_game_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.models.PointsOnGame;
import clickgame.click_game_project.services.GameWebSocketService;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/game")
public class GameController {
    
@Autowired
private GameWebSocketService gameWebSocketService;

@GetMapping("/points")
public PointsOnGame getPointsOnGame(){
    return gameWebSocketService.RandomPoints();

}

@PostMapping("/create")
public ResponseEntity<?> createGame(@RequestBody User user) {
    try {
        Game game = gameWebSocketService.createGame(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

    

}
