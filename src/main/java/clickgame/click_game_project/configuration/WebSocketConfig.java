package clickgame.click_game_project.configuration;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @Override
    public void registerStompEndpoints(@SuppressWarnings("null") StompEndpointRegistry registry) {
        registry.addEndpoint("/game-WS")// punto Websocket por donde se conectan 
                .setAllowedOrigins("*");//permite que se conecte de diversos puntos frontend
                    }

    @SuppressWarnings("null")
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/game");//canal por donde se comunican
        registry.setApplicationDestinationPrefixes("/click");//prefijo que el backend usara a la hora de recibir 
    }


    
}
