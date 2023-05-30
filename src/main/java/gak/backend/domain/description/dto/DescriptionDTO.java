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
        private Long member_id;
        private Long question_id;
        private String content;

        public Description of(Question question){
            return Description.builder()
                    .question(question)
                    .build();
        }
    }
    //quiz정답자나 퀴즈 심플 조회 -> 퀴즈 정답자 조회의 경우 question_id는 뺌
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DescriptionSimpleInfoDTO{
        private Long member_id;
        private String content;
        private Long question_id;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DescriptionStatisticDTO{
        List<List<DescriptionSimpleInfoDTO>> response;
        int[] num;

    }

}
