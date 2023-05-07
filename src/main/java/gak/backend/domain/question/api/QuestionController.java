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



    @GetMapping("/question/UpdateAllQuestion/{FormId}/{QuestionId}")
    public List<Question> getAllQuestion(@PathVariable("FormId")Long FormId,@PathVariable("QuestionId")Long QuestionId){

        return questionService.getAllQuestion(FormId,QuestionId);
    }
    @GetMapping("/question/UpdateSelectQuestion/{QuestionId}")
    public Question getSelectQuestion(@PathVariable("QuestionId")Long QuestionId){

        return questionService.getSelectQuestion(QuestionId);
    }
//    @PutMapping("/question/UpdateAllQuestion/{QuestionId}")
//    public Question updateAllquestion(@RequestBody QuestionDTO questionDTO,@PathVariable("QuestionId")Long id){
//
//        return questionService.UpdateAllQuestion(questionDTO,id);
//
//    }

}
