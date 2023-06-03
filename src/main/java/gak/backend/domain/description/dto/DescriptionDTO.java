package gak.backend.domain.description.dto;


import gak.backend.domain.description.model.Description;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.question.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
public class DescriptionDTO{

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DescriptionSaveRequest{
        //private Long member_id;
        private Long question_id;
        private String content;

        public Description of(Member member, Question question){
            return Description.builder()
                    .member(member)
                    .question(question)
                    .content(this.content)
                    .build();
        }
    }
    //quiz정답자나 퀴즈 심플 조회 -> 퀴즈 정답자 조회의 경우 question_id는 뺌
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DescriptionSimpleInfoDTO{
        private Long id;
        private Long responsor_id;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DescriptionInfoDTO{
        private Long id;
        private Long responsor_id;
        private Long question_id;
        private String content;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DescriptionStatisticDTO{
        List<List<DescriptionSimpleInfoDTO>> response;
        List<String> opList;
        int[] num;

    }

    //원래 응답은 삭제가 안되지만 생성에도 사용되니까 사용자가 삭제를 원할 경우, 삭제
    //응답이 1개라도 있으면 삭제 불가능하게
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DeleteDescriptionDTO{
        private Long responsor_id;
       // private Long descriptionId;
    }

}
