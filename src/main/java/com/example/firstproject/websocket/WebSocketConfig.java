package com.example.firstproject.websocket;

import com.example.firstproject.dto.ChatMessageDTO;
import com.example.firstproject.repository.ChatRepository;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocketMessageBroker
@CrossOrigin("*")
@Log4j2
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final ChatRepository chatRepository;

    public WebSocketConfig(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                /*
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        // add attributes as needed
                        return true;
                    }
                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                               WebSocketHandler wsHandler, Exception ex) {
                        // do something after handshake, if needed
                    }
                })
                */
                .withSockJS();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {

            @Override
            public Message<?> preSend(@Payload Message<?> message, MessageChannel channel) {

                byte[] payloadBytes = (byte[]) message.getPayload();
                String payloadString = new String(payloadBytes);
                ChatMessageDTO chatMessageDTO = new Gson().fromJson(payloadString, ChatMessageDTO.class);
                log.info("Message recieve on inbound channel: " + message, chatMessageDTO);
                return message;
            }
        });
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public void afterSendCompletion(@Payload Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
                byte[] payloadBytes = (byte[]) message.getPayload();
                String payloadString = new String(payloadBytes);
                log.info("Message sent on outbound channel: " + message, payloadString);
            }
        });
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic","/pub");
        config.setApplicationDestinationPrefixes("/app");
    }


}



/*
@Configuration
// prefix가 붙은 메시지를 발행 시 브로커가 처리
@EnableWebSocketMessageBroker
@CrossOrigin("*")
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp")
                .setAllowedOriginPatterns("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
                        // add attributes as needed
                        return true;
                    }

                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                               WebSocketHandler wsHandler, Exception ex) {
                        // do something after handshake, if needed
                    }
                })
                .withSockJS();


    }
    /*
    //클라이언트에서 웹소켓 연결 시 사용할 경로를 설정해주는 메서드
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //stomp websocket 연결 endpoint는 /ws-stomp로 설정. cors 허용도 해줌.
        registry.addEndpoint("/ws-stomp")//이 경로로 접속되는 웹소켓이 stomp 통신임을 확인 & 연결
                .setAllowedOriginPatterns("*")
                .addInterceptors(new StompHandshakeInterceptor())
                .withSockJS()
                .setClientLibraryUrl("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/stomp.js/2.3.3/stomp.min.js\" integrity=\"sha512-iKDtgDyTHjAitUDdLljGhenhPwrbBfqTKWO1mkhSFH3A7blITC9MhYon6SjnMhp4o0rADGw9yAC6EW4t5a4K3g==\" crossorigin=\"anonymous\" referrerpolicy=\"no-referrer\"></script>");
    }



    @Override
    //인메모리 기반 Simple Message Broker를 활성화
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //메시지 구독 요청 url - 메시지 받을 때
        config.enableSimpleBroker("topic"); //1:1 = "/queue", 1:M = "/topic"
        //메시지 발행 요청 url - 메세지 보낼 때
        config.setApplicationDestinationPrefixes("/app");
    }



    private static Set<Session> clients =
            Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session s) {
        System.out.println("웹소켓 연결이 열렸습니다.");
        if(!clients.contains(s)) {
            clients.add(s);
            System.out.println("session open : " + s);
        }else {
            System.out.println("이미 연결된 session 임!!!");
        }
    }

    @OnMessage
    public void onMessage(String msg, Session session) throws Exception{
        System.out.println("receive message : " + msg);
        for(Session s : clients) {
            System.out.println("send data : " + msg);
            s.getBasicRemote().sendText(msg);
        }

    }

    @OnClose
    public void onClose(Session s) {
        System.out.println("session close : " + s);
        clients.remove(s);
    }
}
     */

