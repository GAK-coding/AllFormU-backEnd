package gak.backend.domain.response.application;

import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.response.dao.ResponseRepository;
import gak.backend.domain.response.dto.ResponseDTO;
import gak.backend.domain.response.dto.ResponseDTO.ResponseInfoDTO;
import gak.backend.domain.response.dto.ResponseDTO.ResponseSimpleInfoDTO;
import gak.backend.domain.response.dto.ResponseDTO.SaveResponseRequest;
import gak.backend.domain.response.dto.ResponseDTO.UpdateResponseRequest;
import gak.backend.domain.response.model.Response;
import gak.backend.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;

    //=========================Create===============================
    @Transactional
    public ResponseInfoDTO createResponse(SaveResponseRequest saveResponseRequest){
        //존재하는 멤버, 문제인지 확인
        Member responsor = memberRepository.findById(saveResponseRequest.getResponsorId()).orElseThrow(NotFoundByIdException::new);
        Question question = questionRepository.findById(saveResponseRequest.getQuestionId()).orElseThrow(NotFoundByIdException::new);
        //문제 유형마다 다르게 답변이 저장되어야함.
        if(question.getType().equals("SELECT")){
            //TODO 여기에서 Grid, 복수형과 차별점을 둘 것.
        }
        Response response = saveResponseRequest.toEntity(responsor, question);
        ResponseInfoDTO responseInfoDTO = response.toResponseInfoDTO();

        return responseInfoDTO;
    }

    //==========================Read=================================
    //아마 이부분을 통계에서 쓰지 않을까 생각함.

    //Question아이디로 응답 불러오기
    @Transactional
    public List<ResponseSimpleInfoDTO> findResponsesByQuestionId(Long questionId){
        List<Response> responses = responseRepository.findByQuestionId(questionId);
        List<ResponseSimpleInfoDTO> responsesSimpleInfoDTOs = new ArrayList<>();
        for(Response response : responses){
            ResponseSimpleInfoDTO responseSimpleInfoDTO = response.responseSimpleInfoDTO(response.getResponsor(), response.getQuestion());
            responsesSimpleInfoDTOs.add(responseSimpleInfoDTO);
        }
        return responsesSimpleInfoDTOs;
    }

    //Question아이디로 응답 갯수 불러오기
    @Transactional
    public int countResponsesByQuestionId(Long questionId){
        List<Response> responses = responseRepository.findByQuestionId(questionId);
        int responseCnt = responseRepository.countResponseByQuestionId(questionId);
        return responseCnt;
    }

    //객관식 퀴즈 일 경우, 정답자와 오답자를 출력
    //굳이 자세하게 볼 필요 없을 것 같아서 simpleDTO로 함.
//    @Transactional
//    public List<ResponseSimpleInfoDTO> findQuizRightResponseByQuestionId(Long questionId){
//        List<Response> responses = responseRepository.findByQuestionId(questionId);
//        Question question = questionRepository.findById(questionId).orElseThrow();
//        Selection selection =
//        //quiz인지 확인
//        //TODO question에 quiz 여부를 판단하는 컬럼 추가 해야함.
//        for(Response response : responses){
//            response.
//        }
//
//    }
    //객관식 퀴즈 일 경우, 정답자와 오답자의 수를 출력




    //========================Update===================================
    @Transactional
    public ResponseInfoDTO updateResponse(UpdateResponseRequest updateResponseRequest){
        //response가 존재하는지 확인해야함.
        Response response = responseRepository.findById(updateResponseRequest.getId()).orElseThrow(NotFoundByIdException::new);
        //question의 응답 수정 여부는 form의 상태르 확인해야함.

        response.updateResponse(updateResponseRequest.getChangeNum());
        ResponseInfoDTO responseInfoDTO = response.toResponseInfoDTO();
        return responseInfoDTO;

    }

    //========================Delete==========================
    //response는 delete가 없음. form 작성자가 폼을 삭제할 수 있고 응답자는 자신의 응답을 삭제할 수 없음.

}