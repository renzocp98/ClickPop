package clickgame.click_game_project.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clickgame.click_game_project.entities.Game;
import clickgame.click_game_project.entities.User;
import clickgame.click_game_project.services.GameWebSocketService;
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/game")
public class GameController {
    
@Autowired
private GameWebSocketService gameWebSocketService;


@PostMapping("/create")
public ResponseEntity<?> createGame(@RequestBody User user) {
    try {
        Game game = gameWebSocketService.createGame(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(game);
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

@PutMapping("/score/{score}")
public ResponseEntity<?> AddScore(@RequestBody Game game, @PathVariable int score){
    Optional<Game> gameExist = gameWebSocketService.findById(game.getId());
    if (gameExist.isEmpty()) {
        return ResponseEntity.notFound().build();
    }
    gameWebSocketService.AddScore(gameExist.get(), score);
    return ResponseEntity.ok().build();
}

    

}
