package gak.backend.domain.response.application;

import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Role;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.response.dao.ResponseRepository;
import gak.backend.domain.response.dto.ResponseDTO;
import gak.backend.domain.response.dto.ResponseDTO.*;
import gak.backend.domain.response.exception.CanNotAccessResponse;
import gak.backend.domain.response.model.Response;
import gak.backend.domain.selection.model.Selection;
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
        //TODO Form status update 되면 맞게 수정.
        responsor.UpdateMemberRole(Role.Role_Responsor);
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
            ResponseSimpleInfoDTO responseSimpleInfoDTO = response.toResponseSimpleInfoDTO(response.getResponsor(), response.getQuestion());
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

    //객관식 각각의 인원수와 각각의 응다 여부 파악
    //front는 해당 문제의 객관식 인덱스를 넘겨주는것 번호가 아님 번호는 1부터 시작하지만 index는 0붜터임 -> 이거 통일하는게 나으려나 번호로? 0번부터
    @Transactional
    public List<ResponseSimpleInfoDTO>[] readStatisticByQuestionId(Long questionID){
        List<Response> responses = responseRepository.findByQuestionId(questionID);
        Question question = questionRepository.findById(questionID).orElseThrow(NotFoundByIdException::new);
        //객관식 옵션이 수만큼 새로운 배열을 생성 -> 질문이 수정이 되면 배열의 경우 수를 늘릴 수 없으니까 List가 돠어야한다고 생각했는데 그때그때 새로 초기화 되어서 만들어지니까 상관노일듯
        //int[] countEachResponse = new int[question.getOptions().size()];
        List<ResponseSimpleInfoDTO>[] statistic = new List[question.getOptions().size()];
        //배열의 인덱스가 옵션들의 인덱스
        for(Response response : responses){
            statistic[response.getNum()].add(response.toResponseSimpleInfoDTO(response.getResponsor(), question)); //인덱스에 맞는 count를 증가시킴
        }
        return statistic;
    }

    //객관식 퀴즈 일 경우, 정답자 출력
    //TODO 객관식 중복 정답일 경우 구현
    //굳이 자세하게 볼 필요 없을 것 같아서 simpleDTO로 함. => 인원을 같이 출력하는게 좋을 듯
    @Transactional
    public QuizRightResponseDTO findQuizRightResponseByQuestionId(Long questionId){
        List<Response> responses = responseRepository.findByQuestionId(questionId);
        List<ResponseSimpleInfoDTO> quizRightResponses = new ArrayList<>();
        Question question = questionRepository.findById(questionId).orElseThrow();
        int answer = -1;
        //퀴즈라면, 정답을 체크해놔야함.-> 백엔드에서 퀴즈가 아닐 경우를 처리해줄 필요가 없을 듯 프론트에서 하면 될듯. -> API니까
        for(Selection selection : question.getOptions()){
            if(selection.isAnswer()){
                answer = question.getOptions().indexOf(selection); //정답인 옵션을 찾으면 그 옵션의 인덱스를 저장
            }
        }
        //num이랑 answer가 같으면 정답임 -> 그럼 정답 응답을 list에 넣고 반환
        for(Response response : responses){
            if(response.getNum()==answer){
                quizRightResponses.add(response.toResponseSimpleInfoDTO(response.getResponsor(), question));
            }
        }


        return QuizRightResponseDTO.builder()
                .quizRightResponses(quizRightResponses)
                .rightResponseNum(quizRightResponses.size())
                .build();
    }
//    @Transactional
//    public List<ResponseSimpleInfoDTO> findQuizRightResponseByQuestionId(Long questionId){
//        List<Response> responses = responseRepository.findByQuestionId(questionId);
//        List<ResponseSimpleInfoDTO> rightResponses = new ArrayList<>();
//        Question question = questionRepository.findById(questionId).orElseThrow();
//        int answer = -1;
//        //퀴즈라면, 정답을 체크해놔야함.-> 백엔드에서 퀴즈가 아닐 경우를 처리해줄 필요가 없을 듯 프론트에서 하면 될듯. -> API니까
//        for(Selection selection : question.getOptions()){
//            if(selection.isAnswer()){
//                answer = question.getOptions().indexOf(selection); //정답인 옵션을 찾으면 그 옵션의 인덱스를 저장
//            }
//        }
//        //num이랑 answer가 같으면 정답임 -> 그럼 정답 응답을 list에 넣고 반환
//        for(Response response : responses){
//            if(response.getNum()==answer){
//                rightResponses.add(response.toResponseSimpleInfoDTO(response.getResponsor(), question));
//            }
//        }
//        return rightResponses;
//    }
    //객관식 퀴즈 일 경우, 정답자와 오답자의 수를 출




    //========================Update===================================
    @Transactional
    public ResponseInfoDTO updateResponse(UpdateResponseRequest updateResponseRequest){
        //response가 존재하는지 확인해야함.
        Response response = responseRepository.findById(updateResponseRequest.getId()).orElseThrow(NotFoundByIdException::new);
        Question question = questionRepository.findById(response.getQuestion().getId()).orElseThrow(NotFoundByIdException::new);
        //1차적으로 응답을 수정할 수 있는지 확인하고 할 수 없으면 예외 처리
        //TODO question의 응답 수정 여부는 form의 상태 확인 하는 부분도 추가
        if(question.getForm().isFix()) {

            response.updateResponse(updateResponseRequest.getChangeNum());
            ResponseInfoDTO responseInfoDTO = response.toResponseInfoDTO();
            return responseInfoDTO;
        }
        //에러 넘겨주면 프론트는 그냥 수정창이 안열리게 하면 될듯.
        else{
            throw new CanNotAccessResponse("응답이 이미 제출되었으며, 수정할 수 없는 설문입니다.");
        }

    }

    //========================Delete==========================
    //response는 delete가 없음. form 작성자가 폼을 삭제할 수 있고 응답자는 자신의 응답을 삭제할 수 없음.

}