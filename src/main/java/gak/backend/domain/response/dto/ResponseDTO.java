package gak.backend.domain.response.dto;

import gak.backend.domain.member.model.Member;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.response.model.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ResponseDTO {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveResponseRequest{
        private Long questionId;
        private int num;

        public Response toEntity(Member member, Question question){
            return Response.builder()
                    .responsor(member)
                    .question(question)
                    .num(num)
                    .build();
        }
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseSimpleInfoDTO{
        private Long id;
        private Long responsor_id;
        private Long question_id;
        private int num;
    }
    //체크박스일 경우 자신이 응답한 것들 조회 및 갯수 -> 즉 1번 문제에 1,3,5 총 3개의 답을 보냈다 를 포함하는 정보
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ResponseListInfoDTO{
        private List<ResponseSimpleInfoDTO> responseList;
        private int count;

    }
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatisticResponseDTO{
        private List<List<ResponseSimpleInfoDTO>> responses;
        private int[] nums;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResponseRequest{
        private int changeNum;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QuizRightResponseDTO{
        private List<ResponseSimpleInfoDTO> quizRightResponses;
        private int rightResponseNum;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResponseInfoDTO{
        private Long id;
        private Long responsor_id;
        private Long question_id;
        private int num;
        private LocalDateTime createdTime;
        private LocalDateTime modifiedTime;

    }
}
