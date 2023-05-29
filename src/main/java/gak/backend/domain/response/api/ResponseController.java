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
    @PostMapping(value="/response")
    public ResponseEntity<ResponseInfoDTO> createResponse(@RequestBody @Validated SaveResponseRequest saveResponseRequest){
        ResponseInfoDTO responseInfoDTO = responseService.createResponse(saveResponseRequest);
        return new ResponseEntity<>(responseInfoDTO, HttpStatus.CREATED);

    }
    //===================응답 조회======================
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
    @GetMapping(value="/response/{question_id}/statistic/")
    public ResponseEntity<List<ResponseSimpleInfoDTO>[]> readStatisticByQuestionId(@PathVariable(name="question_id") Long questionId){
        List<ResponseSimpleInfoDTO>[] statistic = responseService.readStatisticByQuestionId(questionId);
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
    public ResponseEntity<ResponseInfoDTO> updateResponse(@PathVariable(name="response_id")Long resposeId,@RequestBody @Validated UpdateResponseRequest updateResponseRequest){
        ResponseInfoDTO responseInfoDTO = responseService.updateResponse(updateResponseRequest);
        return new ResponseEntity<>(responseInfoDTO, HttpStatus.OK);
    }

    //===================응답 삭제 - 없음(필요시 추가 구현)==================

}

