package gak.backend.domain.description.api;

import gak.backend.domain.description.application.DescriptionService;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.dto.DescriptionDTO.DescriptionInfoDTO;
import gak.backend.domain.description.dto.DescriptionDTO.DescriptionSaveRequest;
import gak.backend.domain.description.dto.DescriptionDTO.DescriptionSimpleInfoDTO;
import gak.backend.domain.description.dto.DescriptionDTO.DescriptionStatisticDTO;
import gak.backend.domain.description.model.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.parameters.P;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Map;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor

//응답말고 생성하는 관점에서만 생각
//-> 주관식 생성, 생성한거 get, 질문 수정, 질문 삭제
public class DescriptionController {
    @Autowired
    private final DescriptionService descriptionService;

    //content- 답변자의 정답
    //quiz가 true일때 생성자가 answer에 답을 지정해줘야함
    //quiz가 false일때 answer은 null

    //description생성
    @PostMapping(value = "/description/createDescription/{form_id}/{member_id}")
    public ResponseEntity<String> add(@PathVariable(name = "form_id")Long formId, @PathVariable(name = "member_id")Long memberId, @RequestBody @Validated List<DescriptionSaveRequest> descriptionSaveRequests){
        //int responseCnt = descriptionSaveRequests.size();
        String s = descriptionService.createDescription(formId, memberId,descriptionSaveRequests);
        return new ResponseEntity<>(s,HttpStatus.OK);
    }


    //===============================read===========================

    //memberId와 DescriptionId로 자신이 응답한거 조회
    @GetMapping(value = "/description/{questionId}/{memberId}")
    public ResponseEntity<DescriptionInfoDTO> findResponseByMemberIdAndQuestionId(@PathVariable(name = "questionId")Long questionId, @PathVariable(name = "memberId")Long memberId){
        DescriptionInfoDTO descriptionInfoDTO = descriptionService.findResponseByMemberIdAndQuestionId(memberId, questionId);
        return new ResponseEntity<>(descriptionInfoDTO, HttpStatus.OK);
    }

    //descriptionId로 description 조회
    @GetMapping(value = "/description/getDescription/{description_id}")
    public ResponseEntity<DescriptionInfoDTO> getId(@PathVariable(name = "description_id")Long id){
        DescriptionInfoDTO descriptionInfoDTO = descriptionService.getDescription(id);
        return new ResponseEntity<>(descriptionInfoDTO, HttpStatus.OK);
    }

    //quesstion_id로 description조회
    @GetMapping(value = "/description/{question_id}")
    public ResponseEntity<List<DescriptionInfoDTO>> getIdByQ(@PathVariable(name = "question_id")Long question_id){
        List<DescriptionInfoDTO> descriptionInfoDTO = descriptionService.getDescriptionByQuestionId(question_id);
        return new ResponseEntity<>(descriptionInfoDTO, HttpStatus.OK);
    }

    //해당 문제의 응답자 수 확인
    @GetMapping(value = "/description/statistic/{question_id}/count")
    public ResponseEntity<Integer> countDescriptionsByQuestionId(@PathVariable(name = "question_id") Long quesitonId){
        int num = descriptionService.countDescriptionsByQuestionId(quesitonId);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    //퀴즈 & 통계 -> 정담자 출력
    @GetMapping(value = "/description/quiz/{question_id}")
    public List<DescriptionSimpleInfoDTO> findQuizRightDescriptions(@PathVariable(name = "question_id") Long questionId){
        return descriptionService.findQuizRightDescriptions(questionId);
    }

    //통계 -> 각 항목별 응답자 수와 리스트
    @GetMapping(value = "/description/statistic/{question_id}")
    public DescriptionStatisticDTO statistic(@PathVariable(name = "question_id") Long questionId){
        return descriptionService.statistic(questionId);
    }

    //=====================update=================================

    @PutMapping(value = "/description/updateContent/{questionid}/{descriptionid}")
    public ResponseEntity<Description> updateDescriptionAnswer(@PathVariable(value = "questionid") Long QuestionId,
                                                               @PathVariable(value = "descriptionid") Long DescriptionId,
                                                               @RequestBody Map<String, String> requestBody){

        String content = requestBody.get("content");
        final Description updatedDescription=descriptionService.updateDescription(QuestionId,DescriptionId,content);
        return ResponseEntity.ok(updatedDescription);
    }

    //==================delete======================
    @DeleteMapping(value = "/description/deleteDescription/{description_id}")
    public ResponseEntity<String> deleteId(@PathVariable(name="description_id")Long descriptionId, @RequestBody DescriptionDTO.DeleteDescriptionDTO deleteDescriptionDTO){
        String s = descriptionService.deleteSelectionById(deleteDescriptionDTO, descriptionId);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }

}
