package gak.backend.domain.description.dto;


import gak.backend.domain.description.model.Description;
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
    private boolean quiz;

    private String title;

    @Builder
    public DescriptionDTO(Question question, String content, String answer, Boolean quiz,String title){
        this.question=question;
        this.content=content;
        this.answer=answer;
        this.quiz=quiz;
        this.title=title;
    }

    public Description of(Question question){
        return Description.builder()
                .question(question)
                .title(title)
                .content(content)
                .answer(answer)
                .quiz(quiz)
                .build();
    }
}
