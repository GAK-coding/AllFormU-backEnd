package gak.backend.domain.description.dto;


import gak.backend.domain.question.model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DescriptionDTO {
    private Question question;
    private String content;
    private String answer;
    private Boolean quiz;

    private String title;

    @Builder
    public DescriptionDTO(Question question, String content, String answer, Boolean quiz,String title){
        this.question=question;
        this.content=content;
        this.answer=answer;
        this.quiz=quiz;
        this.title=title;
    }
}
