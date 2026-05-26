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

    @MessageMapping("/registerClick")  // El frontend envía clicks a "/click/registerClick"
    @SendTo("/backsend/score")         // La respuesta del backend se envía a "/backsend/score"
    public ClickGameResponse handlerClicks(ClickGameMessage clickGameMessage) {
        int gameId = clickGameMessage.getGameId();
        int x      = clickGameMessage.getX();
        int y      = clickGameMessage.getY();

        boolean isValid = gameWebSocketService.clickValidation(gameId, x, y);
        int score       = gameWebSocketService.controlScore(gameId, isValid);

        return new ClickGameResponse(isValid, score);
    }

}
