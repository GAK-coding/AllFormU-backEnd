package gak.backend.domain.form.api;

import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
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


    /*
        프론트에서 질문 생성 시 form 생성 service 호출

    */
    @PostMapping("/api/createform/{AuthorId}")
    public String create(@RequestBody FormDTO formDTO,@PathVariable("AuthorId")Long id){
        System.out.println("controller:"+formDTO.getContent());
        formService.createForm(formDTO,id);
        return "Success for create form";
    }

    /*
        모든 설문지 조회
    */
    @GetMapping("/api/find")
    public List<Form> get(){

        return formService.getAllForms();
    }

    /*
        설문지 id로 설문지 조회
        Member 완성 시 Member id로 설문지 조회 기능 추가
    */
    @GetMapping("/api/find/{AuthorId}")
    public List<Form> getId(@PathVariable("AuthorId")Long authorid){

        return formService.getFormById(authorid);

    }
    @GetMapping("/api/find/{AuthorId}/{FormId}")
    public Form getSelectForm(@PathVariable("AuthorId")Long authorid,@PathVariable("FormId")Long Formid){

        return formService.getSelectFormById(authorid,Formid);

    }
//    @PostMapping("/api/update")
//    public Form getId(@RequestBody FormDTO formDTO,@PathVariable("id")Long id){
//        return formService.updateForm(id,formDTO);
//    }
    /*
        user id로 모든설문지 삭제
    */
    @DeleteMapping("/api/delete/{id}")
    public String deleteId(@PathVariable("id")Long id){
        formService.deleteFormById(id);

        return String.format("Success for delete %s form",id);
    }

    /*
        user id의 formid에 해당되는 설문지 삭제
    */
    @DeleteMapping("/api/delete/{id}/{FormId}")
    public String deleteSelectId(@PathVariable("id")Long id,@PathVariable("FormId")Long Formid){
        formService.deleteSelectFormById(id,Formid);

        return String.format("Success for delete %s form of userId %s",Formid,id);
    }


    /*
        모든 설문지 삭제
    */
    @DeleteMapping("/api/delete")
    public String delete(){
        formService.deleteForm();
        return "Success for delete form";
    }


}
