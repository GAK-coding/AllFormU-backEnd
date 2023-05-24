package gak.backend.domain.selection.dto;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.model.Selection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@NoArgsConstructor
public class SelectionDTO {

    private Question question;
    private String content;

    private boolean answer;
//    private Boolean quiz;

    @Builder
    public SelectionDTO(Question question, String content, boolean answer){
        this.question=question;
        this.content=content;
        this.answer=answer;
//        this.quiz=quiz;
    }

    public Selection of (Question question){
        return Selection.builder()
                .question(question)
                .content(content)
                .answer(answer)
                .build();
    }





}
