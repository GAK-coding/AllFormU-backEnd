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

    private String title;

    @Builder
    public DescriptionDTO(String content, String answer, Boolean quiz,String title){
        this.content=content;
        this.answer=answer;
        this.quiz=quiz;
        this.title=title;
    }
}
