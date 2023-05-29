package gak.backend.domain.response.application;

import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.model.Correspond;
import gak.backend.domain.form.model.Form;
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
    private final FormRepository formRepository;

    //=========================Create===============================
    @Transactional
    public ResponseInfoDTO createResponse(SaveResponseRequest saveResponseRequest){
        //존재하는 멤버, 문제인지 확인
        Member responsor = memberRepository.findById(saveResponseRequest.getResponsorId()).orElseThrow(NotFoundByIdException::new);
        Question question = questionRepository.findById(saveResponseRequest.getQuestionId()).orElseThrow(NotFoundByIdException::new);
        Form form = formRepository.findById(question.getForm().getId()).orElseThrow(NotFoundByIdException::new);
        if(form.getCorrespond()==Correspond.STATUS_PROCESS) {
            responsor.UpdateMemberRole(Role.Role_Responsor);
            //문제 유형마다 다르게 답변이 저장되어야함.
            if (question.getType().equals("SELECT")) {
                //TODO 여기에서 Grid, 복수형과 차별점을 둘 것.
            }
            Response response = saveResponseRequest.toEntity(responsor, question);
            ResponseInfoDTO responseInfoDTO = response.toResponseInfoDTO();

            return responseInfoDTO;
        }
        else{
            throw new CanNotAccessResponse("설문 응답 시간이 아닙니다.");
        }
    }

    //==========================Read=================================
    //아마 이부분을 통계에서 쓰지 않을까 생각함.

    //Question아이디로 응답 불러오기
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public int countResponsesByQuestionId(Long questionId){
        List<Response> responses = responseRepository.findByQuestionId(questionId);
        int responseCnt = responseRepository.countResponseByQuestionId(questionId);
        return responseCnt;
    }

    //객관식 각각의 인원수와 각각의 응답 여부 파악
    //front는 해당 문제의 객관식 인덱스를 넘겨주는것 번호가 아님 번호는 1부터 시작하지만 index는 0붜터임 -> 이거 통일하는게 나으려나 번호로? 0번부터
    @Transactional(readOnly = true)
    public StatisticResponseDTO readStatisticByQuestionId(Long questionID){
        List<Response> responses = responseRepository.findByQuestionId(questionID);
        Question question = questionRepository.findById(questionID).orElseThrow(NotFoundByIdException::new);
        //객관식 옵션이 수만큼 새로운 배열을 생성 -> 질문이 수정이 되면 배열의 경우 수를 늘릴 수 없으니까 List가 돠어야한다고 생각했는데 그때그때 새로 초기화 되어서 만들어지니까 상관노일듯
        //int[] countEachResponse = new int[question.getOptions().size()];
        List<ResponseSimpleInfoDTO>[] statistic = new List[question.getOptions().size()];
        //배열의 인덱스가 옵션들의 인덱스
        for(Response response : responses){
            statistic[response.getNum()].add(response.toResponseSimpleInfoDTO(response.getResponsor(), question)); //인덱스에 맞는 count를 증가시킴
        }
        //각각의 갯수 계산
        int[] nums = new int[question.getOptions().size()];
        for(int i = 0 ; i < question.getOptions().size(); i++){
            nums[i] = statistic[i].size();
        }
        return StatisticResponseDTO.builder()
                .responses(statistic)
                .nums(nums)
                .build();
    }

    //객관식 퀴즈 일 경우, 정답자 출력
    //TODO 객관식 중복 정답일 경우 구현
    //굳이 자세하게 볼 필요 없을 것 같아서 simpleDTO로 함. => 인원을 같이 출력하는게 좋을 듯
    @Transactional(readOnly = true)
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
    public ResponseInfoDTO updateResponse(Long responseId, UpdateResponseRequest updateResponseRequest){
        //response가 존재하는지 확인해야함.
        Response response = responseRepository.findById(responseId).orElseThrow(NotFoundByIdException::new);
        Question question = questionRepository.findById(response.getQuestion().getId()).orElseThrow(NotFoundByIdException::new);
        //폼의 수정 여부를 불러오기 위해 찾아놓기
        Form form = formRepository.findById(question.getForm().getId()).orElseThrow(NotFoundByIdException::new);

        //1차적으로 응답을 수정할 수 있는지 확인하고 할 수 없으면 예외 처리( form의 응답 수정 여부와
        //form이 수정가능한지 여부를 먼저 확인해야함 -> 그 다음에 폼의 상태로 유효함을 판단하는게 로직상 맞음.
        if(form.isFix()) { //응답이 수정 가능 하고 설문이 유효한 경우
            if(form.getCorrespond()== Correspond.STATUS_PROCESS) {// 사실 조건 검사를 &&로 묶어도 되나 예외를 세분화 하기 위해서 두개의 if문을 사용함.
                response.updateResponse(updateResponseRequest.getChangeNum());
                ResponseInfoDTO responseInfoDTO = response.toResponseInfoDTO();
                return responseInfoDTO;
            }
            else{ //응답 수정은 가능하나 , 상태가 맞지 않아서 불가능 한 경우
                throw new CanNotAccessResponse("설문 응답 시간이 아닙니다.");

            }
        }
        //에러 넘겨주면 프론트는 그냥 수정창이 안열리게 하면 될듯.
        else{ //수정이 아예 불가능 한 경우
            throw new CanNotAccessResponse("응답이 이미 제출되었으며, 수정할 수 없는 설문입니다.");
        }

    }

    //========================Delete==========================
    //response는 delete가 없음. form 작성자가 폼을 삭제할 수 있고 응답자는 자신의 응답을 삭제할 수 없음.

}