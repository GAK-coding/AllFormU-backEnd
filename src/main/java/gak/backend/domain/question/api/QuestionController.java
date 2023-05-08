package gak.backend.domain.question.api;

import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.model.Selection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;


        /*
            form은 만들어져 있고 form 안에 모든 question이 제거 되었을 때
            question만 추가하는 작업 필요.
        */
//    @PostMapping("/question/createQuestion/{FormId}")
//    public Question createQuestion(@RequestBody QuestionDTO questionDTO,@PathVariable("FormId")Long FormId){
//
//
//        return questionService.createQuestion(questionDTO,FormId);
//    }

        /*
            formId에 해당하는 모든 질문을 조회
        */
    @GetMapping("/question/getAllQuestion/{FormId}/{QuestionId}")
    public List<Question> getAllQuestion(@PathVariable("FormId")Long FormId,@PathVariable("QuestionId")Long QuestionId){

        return questionService.getAllQuestion(FormId,QuestionId);
    }
        /*
            formId에 해당하는 특정 question 조회
        */
    @GetMapping("/question/getSelectQuestion/{FormId}/{QuestionId}")
    public Question getSelectQuestion(@PathVariable("FormId")Long FormId,@PathVariable("QuestionId")Long QuestionId){

        return questionService.getSelectQuestion(FormId,QuestionId);
    }

        /*
            formId에 해당하는 특정 question 의 value 수정
            (selection과 같은 하위 list 제외한)
        */
    @PutMapping("/question/UpdateSelectQuestion/{FormId}/{QuestionId}")
    public QuestionDTO updateSelectlquestion(@RequestBody QuestionDTO questionDTO,@PathVariable("FormId")Long FormId,@PathVariable("QuestionId")Long QuestionId){

        return questionService.UpdateSelectQuestion(questionDTO,FormId,QuestionId);

    }
        /*
            formId에 해당하는 모든 question 삭제
            (selection과 같은 하위 list 포함한)
        */

    @DeleteMapping("/question/DeleteAllquestion/{FormId}")
    public String DeleteAllquestion(@PathVariable("FormId")Long FormId){
        questionService.DeleteAllQuestion(FormId);
        return String.format("Success for delete All question of %s form",FormId);
    }
        /*
            formId에 해당하는 특정 question 제거
            (selection과 같은 하위 list 포함한)
        */

    @DeleteMapping("/question/DeleteSelectquestion/{FormId}/{QuestionId}")
    public String DeleteSelectquestion(@PathVariable("FormId")Long FormId,@PathVariable("QuestionId")Long QuestionId){
        questionService.DeleteSelectQuestion(FormId,QuestionId);
        return String.format("Success for delete %s question of %s form",QuestionId,FormId);
    }

}
