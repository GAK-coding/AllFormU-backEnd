package gak.backend.domain.chatgpt.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String content;
    private MessageType type;

    private String question;


    private String sender;

    public enum MessageType{
        CHAT,
        JOIN,
        LEAVE
    }
}

