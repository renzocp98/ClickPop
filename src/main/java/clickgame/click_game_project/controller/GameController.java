package clickgame.click_game_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import clickgame.click_game_project.services.GameWebSocketService;

@RestController
@RequestMapping("/game")
public class GameController {
    
    @Autowired
    private GameWebSocketService gameWebSocketService;

}
