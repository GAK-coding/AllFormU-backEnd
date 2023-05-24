package gak.backend.domain.selection.dto;
import gak.backend.domain.question.model.Question;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class SelectionDTO {

    private Question question;
    private String content;

    private Boolean answer;
//    private Boolean quiz;

    @Builder
    public SelectionDTO(Question question, String content, Boolean answer){
        this.question=question;
        this.content=content;
        this.answer=answer;
//        this.quiz=quiz;
    }


}
