package gak.backend.domain.selection.dto;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.model.Selection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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


        //밖으로 빼도 될 것 같음 -> 빼면 안 됨
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
    @Getter
    @NoArgsConstructor
    @Builder
    @AllArgsConstructor
    public static class LinearInfoDTO{

        /*
        납득이 안가는 에러가 발생했는데
        처음에는 해당 변수명을 LinearList로 설정해놓으니 계속 해당 DTO에 값이 안들어갔음
        근데 변수 접근을 public으로 바꾸니까 또 값이 들어감
        근데 다른 dto는 private으로 해도 @Getter를 해놨으니 당연히 값이 들어가야 하는 건데
        변수명을 아래와 같이 content로 바꾸니까 private일 때 값이 잘 들어감
        무슨 상황인지 모르겠어서 찾아보니 변수명에 따라서 이런 에러가 발생할 수 있다고 한다.
        * */
        private List<String> content;

    }



}
