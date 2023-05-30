package gak.backend.domain.response.model;

import gak.backend.domain.member.model.Member;
import gak.backend.domain.model.BaseTime;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.response.dto.ResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.util.List;

import static gak.backend.domain.response.dto.ResponseDTO.*;

@Entity
@Getter
@Slf4j
//@AllArgsConstructor
@NoArgsConstructor
public class Response extends BaseTime {
    @Id
    @GeneratedValue
    @Column(name="response_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member responsor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="question_id")
    private Question question;

    private int num;

    @Builder
    public Response(Long id, Member responsor, Question question, int num ){
        this.id = id;
        this.responsor = responsor;
        this.question = question;
        this.num = num;
    }

    public ResponseInfoDTO toResponseInfoDTO(){
        return ResponseInfoDTO.builder()
                .id(this.id)
                .responsorId(this.getResponsor().getId())
                .questionId(this.getQuestion().getId())
                .num(this.num)
                .createdTime(super.getCreatedDate())
                .modifiedTime(super.getModifiedDate())
                .build();
    }

    public ResponseSimpleInfoDTO toResponseSimpleInfoDTO(Member responsor, Question question){
        return ResponseSimpleInfoDTO.builder()
                .responsorId(responsor.getId())
                .questionId(question.getId())
                .num(this.num)
                .build();
    }

    public QuizRightResponseDTO toQuizRightResponseDTO(List<ResponseSimpleInfoDTO> quizRightResponses, int rightResponseNum){
        return QuizRightResponseDTO.builder()
                .quizRightResponses(quizRightResponses)
                .rightResponseNum(rightResponseNum)
                .build();
    }
    public void updateResponse(int changeNum){
        this.num = changeNum;
    }

}
