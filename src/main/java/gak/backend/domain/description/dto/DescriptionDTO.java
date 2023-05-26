package gak.backend.domain.description.dto;


import gak.backend.domain.description.model.Description;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.question.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
    //quiz정답자나 퀴즈 심플 조회 -> 퀴즈 정답자 조회의 경우 question_id는 뺌
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DescriptionSimpleInfoDTO{
        private Long member_id;
        private Long question_id;
    }
    public Description of(Question question){
        return Description.builder()
                .question(question)
                .build();
    }

}
