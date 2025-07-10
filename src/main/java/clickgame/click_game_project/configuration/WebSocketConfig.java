package clickgame.click_game_project.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer{

    @SuppressWarnings("null")
    @Override
    public void registerStompEndpoints( StompEndpointRegistry registry) {
        registry.addEndpoint("/game-WS")// punto Websocket por donde se conectan 
                .setAllowedOriginPatterns("*")
                .withSockJS();//permite que se conecte de diversos puntos frontend
                    }

    @SuppressWarnings("null")
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/backsend");//canal por donde se comunican
        registry.setApplicationDestinationPrefixes("/click");//prefijo que el backend usara a la hora de recibir 
    }


    
}
