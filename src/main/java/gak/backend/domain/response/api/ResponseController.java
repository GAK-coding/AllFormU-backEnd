package gak.backend.domain.response.api;

import gak.backend.domain.response.application.ResponseService;
import gak.backend.domain.response.dto.ResponseDTO;
import gak.backend.domain.response.dto.ResponseDTO.*;
import gak.backend.domain.response.model.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class ResponseController {
    private final ResponseService responseService;

    //====================응답 생성==================
    @PostMapping(value="/response/{form_id}/{responsor_id}")
    public ResponseEntity<String> createResponse(@PathVariable(name = "form_id")Long formId, @PathVariable(name = "responsor_id")Long responsorId, @RequestBody @Validated List<SaveResponseRequest> saveResponseRequests){
        String s = responseService.createResponse(formId, responsorId, saveResponseRequests);
        return new ResponseEntity<>(s, HttpStatus.CREATED);

    }
//    @PostMapping(value="/response")
//    public ResponseEntity<ResponseInfoDTO> createResponse(@RequestBody @Validated SaveResponseRequest saveResponseRequest){
//        ResponseInfoDTO r  = responseService.createResponse(saveResponseRequest);
//        return new ResponseEntity<>(r, HttpStatus.CREATED);
//
//    }
    //===================응답 조회======================

    //checkbox용 응답에 대한 결과 확인하기 , 근데 객관식도활용 가능함.
    @GetMapping(value="/response/{question_id}/{responsor_id}")
    public ResponseListInfoDTO findResponseByMemberIdAndQuestionId(@PathVariable(name="question_id")Long questionId, @PathVariable(name="responsor_id")Long responsorId){
        ResponseListInfoDTO responseListInfoDTO = responseService.findResponsesByMemberIdAndQuestionId(responsorId,questionId);
        return responseListInfoDTO;
    }
    //questionId로 응답들 불러오기
    @GetMapping(value = "/response/{question_id}")
    public ResponseEntity<List<ResponseSimpleInfoDTO>> findResponseByQuestionId(@PathVariable(name="question_id") Long questionId){
        List<ResponseSimpleInfoDTO> responseSimpleInfoDTOs = responseService.findResponsesByQuestionId(questionId);
        return new ResponseEntity<>(responseSimpleInfoDTOs, HttpStatus.OK);
    }
    //Question아이디로 응답 갯수 불러오기
    @GetMapping(value="/response/{question_id}/count")
    public ResponseEntity<Integer> countResponseByQuestionId(@PathVariable(name="question_id")Long questionId){
        int responseCnt = responseService.countResponsesByQuestionId(questionId);
        return new ResponseEntity<>(responseCnt, HttpStatus.OK);
    }

    //객관식 옵션별 응답 여부 파악
    @GetMapping(value="/response/{question_id}/statistic")
    public ResponseEntity<StatisticResponseDTO> readStatisticByQuestionId(@PathVariable(name="question_id") Long questionId){
        StatisticResponseDTO statistic = responseService.readStatisticByQuestionId(questionId);
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }
    //객관식일 경우, 정답자 출력
    @GetMapping(value="/response/{question_id}/quiz")
    public ResponseEntity<QuizRightResponseDTO> findQuizRightResponseByQuestionId(@PathVariable(name="question_id")Long questionId){
        QuizRightResponseDTO quizRightResponseDTO = responseService.findQuizRightResponseByQuestionId(questionId);
        return new ResponseEntity<>(quizRightResponseDTO, HttpStatus.OK);
    }

    //==================응답 수정======================
    @PatchMapping(value= "/response/{response_id}")
    public ResponseEntity<ResponseSimpleInfoDTO> updateResponse(@PathVariable(name="response_id")Long responseId,@RequestBody @Validated UpdateResponseRequest updateResponseRequest){
        ResponseSimpleInfoDTO responseSimpleInfoDTO = responseService.updateResponse(responseId,updateResponseRequest);
        return new ResponseEntity<>(responseSimpleInfoDTO, HttpStatus.OK);
    }

    //===================응답 삭제 - 없음(필요시 추가 구현)==================

}

