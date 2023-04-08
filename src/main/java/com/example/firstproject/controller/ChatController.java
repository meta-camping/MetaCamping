package com.example.firstproject.controller;

import com.example.firstproject.dto.ChatMessageDTO;
import com.example.firstproject.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Log4j2
@CrossOrigin("*")
public class ChatController {
    //메시지가 들어왔을 때,
    // 메시지의 데스티네이션 헤더와 메시지 매핑에 설정된 경로가 일치한 핸들러를 찾고
    // 그 핸들러가 요청을 처리


    private final SimpMessagingTemplate template;
    private final ChatService chatService;

    @MessageMapping("/chat/{room_id}")
    @SendTo("/topic/chat/{room_id}")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO message, @DestinationVariable("room_id") String room_id) {
        log.info("컨트롤러 동작!!!!!!!!!!!!!!!!!");
        if ("ENTER".equals(message.getType())) {
            String enterMessage = message.getSender() + "님이 채팅방에 참여하였습니다.";
            template.convertAndSend("/topic/chat/" + message.getRoom_id(), enterMessage);
        } else {
            chatService.saveChat(message);
            template.convertAndSend("/topic/chat/" + message.getRoom_id(), message);
        }
        return message;
    }
}
        /*
        if ("ENTER".equals(message.getType())) {
            String enterMessage = message.getSender() + "님이 채팅방에 참여하였습니다.";
            template.convertAndSend("/topic/chat/" + message.getRoom_id(), enterMessage);
        } else {
            chatService.saveChat(message);
            template.convertAndSend("/topic/chat/" + message.getRoom_id(), message);
        }

         */



    /*
    @MessageMapping(value ="/chat/message")
    @SendTo("/topic/chat/room/{room_id}")
    public void enter(@Payload ChatMessageDTO message) {

        message.setMessage(message.getSender() + "님이 채팅방에 참여하였습니다.");
        String enterMessage = message.getSender() + "님이 채팅방에 참여하였습니다.";
        template.convertAndSend("topic/chat/" + message.getRoom_id(), enterMessage);
        // send the message to all subscribers of the topic
    }

    @MessageMapping("/chat/message")
    @SendTo("/topic/chat/room/{room_id}")
    public ChatMessageDTO sendMessage(@Payload ChatMessageDTO message) {
        chatService.saveChat(new ChatMessageDTO<>());
        template.convertAndSend("topic/chat/" + message.getRoom_id(), message);
        return message;
    }

     */

/*
    @MessageMapping(value ="/chat/message")
    public ChatMessageDTO message (@Payload ChatMessageDTO message){
        template.convertAndSend("app/chat/room/"+ message.getRoom_id(),message);
        chatService.saveChat(message);
        //메세지 확인 후 디비에 밀어넣는 코드 추가할 것!
        log.info(message.getSender() + "님의 메세지:" , message);
        System.out.println("Received message: " + message);

        return message;

    }

 */

    //메세지 매핑 경로는 "chat/message"지만 WebsocketConfig의 Destination Prefix가 "/app"이므로
    //프론트에서 보내는 실질 경로는 "/api/chat/message"가 됨

    //클라이언트의 메시지 요청: "/app/chat/message"
    // => 메세지 컨트롤러가 받아서 "topic/chat/room/{roomId}" 구독하고 있는
    //클라이언트들에게 메세지 전달.
    /*
    private final SimpMessageSendingOperations sendingOperations;
    @MessageMapping("/chat/message")
    @SendTo("/topic/{room_id}")

    public void enter(ChatMessage message) {
        if (ChatMessage.MessageType.ENTER.equals(message.getType())) {
            message.setMessage(message.getSender()+"님이 입장하였습니다.");
        }
        sendingOperations.convertAndSend("/topic/"+message.getRoom_id(),message);
    }
     */
