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
    @PostMapping("/api/createform")
    public String create(@RequestBody FormDTO formDTO){
        System.out.println("controller:"+formDTO.getContent());
        formService.createForm(formDTO);
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
    @GetMapping("/api/find/{id}")
    public Form getId(@PathVariable("id")Long id){

        return formService.getFormById(id);

    }
    /*
        설문지 id로 해당 설문지 삭제
        Member 완성 시 Member id로 설문지 삭제 기능 추가
    */
    @DeleteMapping("/api/delete/{id}")
    public String deleteId(@PathVariable("id")Long id){
        formService.deleteFormById(id);

        return String.format("Success for delete %s form",id);
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
