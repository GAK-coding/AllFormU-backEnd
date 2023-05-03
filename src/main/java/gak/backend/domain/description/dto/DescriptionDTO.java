package gak.backend.domain.description.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DescriptionDTO {
    private String content;
    private String answer;
    private Boolean quiz;

    @Builder
    public DescriptionDTO(String content, String answer, Boolean quiz){
        this.content=content;
        this.answer=answer;
        this.quiz=quiz;
    }
}
