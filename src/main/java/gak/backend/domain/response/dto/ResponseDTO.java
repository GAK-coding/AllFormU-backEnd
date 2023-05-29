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
        private Long responsorId;
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
        private Long responsorId;
        private Long questionId;
        private int num;
    }
    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StatisticResponseDTO{
        private List<ResponseSimpleInfoDTO>[] responses;
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
        private Long responsorId;
        private Long questionId;
        private int num;
        private LocalDateTime createdTime;
        private LocalDateTime modifiedTime;

    }
}
