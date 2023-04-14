// package tfip.akimori.server.configs;

// import java.net.http.WebSocket;
// import java.util.List;

// import org.springframework.context.annotation.Configuration;
// import org.springframework.core.env.Environment;
// import org.springframework.web.socket.config.annotation.EnableWebSocket;
// import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
// import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;
// import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

// import tfip.akimori.server.handler.SignalingHandler;

// @Configuration
// @EnableWebSocket
// public class WebSocketConfig implements WebSocketConfigurer {

//     private SignalingHandler socketHandler; // where to get this
//     private Environment env;

//     // CONSTRUCTOR
//     public WebSocketConfig(SignalingHandler socketHandler, Environment env) {
//         this.socketHandler = socketHandler;
//         this.env = env;
//     }

//     @Override
//     public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//         // what is /signalingsocket
//         WebSocketHandlerRegistration handlerRegistration = registry.addHandler(this.socketHandler, "/signalingsocket");
//         if (List.of(this.env.getActiveProfiles()).stream()
//                 .noneMatch(myProfile -> myProfile.toLowerCase().contains("prod"))) {
//                     // crossorigin??
//                     handlerRegistration.setAllowedOrigins("*");
//         }
//     }

// }
