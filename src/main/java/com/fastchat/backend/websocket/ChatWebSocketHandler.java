package com.fastchat.backend.websocket;

import com.fastchat.backend.model.Message;
import com.fastchat.backend.repository.MessageRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final MessageRepository messageRepository;

    public ChatWebSocketHandler(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        Message chatMessage = new Message();
        chatMessage.setSender("user");
        chatMessage.setContent(message.getPayload());
        chatMessage.setTimestamp(new Date());
        messageRepository.save(chatMessage);

        for (WebSocketSession wsSession : sessions) {
            wsSession.sendMessage(new TextMessage(chatMessage.getContent()));
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

}
