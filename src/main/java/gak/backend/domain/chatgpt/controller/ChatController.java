package gak.backend.domain.chatgpt.controller;

import gak.backend.domain.chatgpt.model.ChatGptResponseDto;
import gak.backend.domain.chatgpt.model.ChatMessage;
import gak.backend.domain.chatgpt.model.Choice;
import gak.backend.domain.chatgpt.model.QuestionRequestDto;
import gak.backend.domain.chatgpt.service.ChatGptService;
import io.github.flashvayne.chatgpt.service.ChatgptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import io.github.flashvayne.chatgpt.dto.ChatRequest;
import io.github.flashvayne.chatgpt.dto.ChatResponse;

import java.util.List;

@Controller
public class ChatController {

    private final ChatGptService chatGptService;
    public ChatController (ChatGptService chatGptService){
        this.chatGptService=chatGptService;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage)
    {
        List<Choice> sentence=chatGptService.askQuestion(chatMessage).getChoices();
        chatMessage.setContent(sentence.get(0).getText());
        return chatMessage;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor){

        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }
}

