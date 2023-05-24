package gak.backend.domain.form.api;

import gak.backend.domain.description.application.DescriptionService;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.selection.application.SelectionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;
    private final QuestionService questionService;

    /*
        프론트에서 질문 생성 시 form 생성 service 호출 -> form과 question만듦
        questionservice 호출 -> selection과 description 따위의 하위 질문 객체들 만듦

    */
    @PostMapping("/form/createform/{UserId}")
    public Long create(@RequestBody FormDTO formDTO,@PathVariable("UserId")Long id){

        Long FormId=formService.createForm(formDTO,id);

        return questionService.createInit(formDTO,id,FormId);
    }

    /*
        모든 설문지 조회
    */
    @GetMapping("/form/findform")
    public List<Form> get(){

        return formService.getAllForms();
    }

    /*
        설문지 id로 설문지 조회
        Member 완성 시 Member id로 설문지 조회 기능 추가
    */
    @GetMapping("/form/findform/{UserId}")
    public List<Form> getId(@PathVariable("UserId")Long authorid){

        return formService.getFormById(authorid);

    }
    @GetMapping("/form/findform/{UserId}/{FormId}")
    public Form getSelectForm(@PathVariable("UserId")Long authorid,@PathVariable("FormId")Long Formid){

        return formService.getSelectFormById(authorid,Formid);

    }
    @PutMapping("/form/updateSelectform/{UserId}/{FormId}")
    public FormDTO getId(@RequestBody FormDTO formDTO,@PathVariable("UserId")Long userid,@PathVariable("FormId")Long formid){
        return formService.updateSelectForm(formDTO,userid,formid);
    }
    /*
        user id로 모든설문지 삭제
    */
    @DeleteMapping("/form/deleteform/{UserId}")
    public String deleteId(@PathVariable("UserId")Long Userid){
        formService.deleteFormById(Userid);

        return String.format("Success for delete all forms of User %s ",Userid);
    }

    /*
        user id의 formid에 해당되는 설문지 삭제
    */
    @DeleteMapping("/form/deleteform/{UserId}/{FormId}")
    public String deleteSelectId(@PathVariable("UserId")Long Userid,@PathVariable("FormId")Long Formid){
        formService.deleteSelectFormById(Userid,Formid);

        return String.format("Success for delete %s form of userId %s",Formid,Userid);
    }


    /*
        모든 설문지 삭제
    */
    @DeleteMapping("/form/deleteform")
    public String delete(){
        formService.deleteForm();
        return "Success for delete form";
    }


}
