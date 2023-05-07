package gak.backend.domain.chatgpt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Getter
@NoArgsConstructor
public class Choice implements Serializable {

    private String text;
    private Integer index;
    @JsonProperty("finish_reason")
    private String finishReason;

    @Builder
    public Choice(String text, Integer index, String finishReason){
        this.text=text;
        this.index=index;
        this.finishReason=finishReason;
    }
    public String getjsonFormat() {
        return "{"+"\"text\":"+text+","+"\"index\":"+index+"}";
    }

}

