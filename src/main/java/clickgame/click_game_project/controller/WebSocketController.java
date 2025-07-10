package clickgame.click_game_project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import clickgame.click_game_project.models.ClickGameMessage;
import clickgame.click_game_project.models.ClickGameResponse;
import clickgame.click_game_project.services.GameWebSocketService;

@Controller
public class WebSocketController {

    @Autowired
    private GameWebSocketService gameWebSocketService;

    @MessageMapping("/registerClick")// El frontend envía clicks a "/click/registerClick"
    @SendTo("/backsend/game")// La respuesta del backend se envía a "/backsend/game"
    private ClickGameResponse handlerClicks (ClickGameMessage clickGameMessage){
        int x = clickGameMessage.getX();
        int y = clickGameMessage.getY();
        boolean isValid = gameWebSocketService.clickValidation(x, y);
        int points = gameWebSocketService.controlScore(x, y);

        if (!isValid) {
            return new ClickGameResponse(false, points);
        }
        return new ClickGameResponse(true, points);
    
    }


}
