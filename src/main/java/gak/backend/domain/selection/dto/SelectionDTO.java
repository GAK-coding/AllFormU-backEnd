package gak.backend.domain.selection.dto;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.model.Selection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SelectionDTO {

    private Question question;
    private String content;

    private boolean answer;
//    private Boolean quiz;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AllSelectionData {
        private Question question;
        private String content;
        private boolean answer;

//        this.quiz=quiz;


        public Selection of(Question question) {
            return Selection.builder()
                    .question(question)
                    .content(content)
                    .answer(answer)
                    .build();
        }

    }

    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class SelectionInfoDTO{
        private Long id;
        private String content;

    }



}
