package gak.backend.domain.chatgpt.service;


import gak.backend.domain.chatgpt.config.ChatGptConfig;
import gak.backend.domain.chatgpt.model.ChatGptRequestDto;
import gak.backend.domain.chatgpt.model.ChatGptResponseDto;
import gak.backend.domain.chatgpt.model.ChatMessage;
import gak.backend.domain.chatgpt.model.QuestionRequestDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatGptService {

    private static RestTemplate restTemplate=new RestTemplate();

    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto){
        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(ChatGptConfig.MEDIA_TYPE));
        headers.add(ChatGptConfig.AUTHORIZATION, ChatGptConfig.BEARER + ChatGptConfig.API_KEY);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto>chatGptRequestDtoHttpEntity){
        ResponseEntity<ChatGptResponseDto> responseEntity=restTemplate.postForEntity(
                ChatGptConfig.URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class
        );
        return responseEntity.getBody();
    }
    public ChatGptResponseDto askQuestion(ChatMessage requestDto){
        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                ChatGptConfig.MODEL,
                                requestDto.getContent(),
                                ChatGptConfig.MAX_TOKEN,
                                ChatGptConfig.TEMPERATURE,
                                ChatGptConfig.TOP_P
                        )
                )
        );
    }
}


