package gak.backend.domain.form.api;

import com.google.firebase.auth.FirebaseAuthException;
import gak.backend.domain.description.application.DescriptionService;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.selection.application.SelectionService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class FormController {
    private final FormService formService;
    private final QuestionService questionService;
    private final FormRepository formRepository;





    @GetMapping("form/pages/")
    public List<FormDTO.PagingDTO> Paging(@RequestParam("page")Long page){


        return formService.Paging(page);
    }

    @PostMapping("/files")
    public String uploadFile(@RequestParam("file")MultipartFile file, String nameFile) throws IOException, FirebaseAuthException{
        if(file.isEmpty()){
            return "is empty";
        }
        return formService.uploadFiles(file, nameFile);
    }

    @GetMapping(value="/images/{fileName}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName){
        byte[] imageData = formService.getImage(fileName);
        System.out.println("BYTE: "+imageData);
        if (imageData != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    /*
        프론트에서 질문 생성 시 form 생성 service 호출 -> form과 question만듦
        questionservice 호출 -> selection과 description 따위의 하위 질문 객체들 만듦

    */
    @PostMapping("/form/createform/{UserId}")
    public Long create(@RequestBody FormDTO.AllFormData allFormData, @PathVariable("UserId")Long id){

        Long FormId=formService.createForm(allFormData,id);

        return questionService.createInit(allFormData,id,FormId);
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
    public Form getId(@RequestBody FormDTO.UpdateFormData updateFormData, @PathVariable("UserId")Long userid, @PathVariable("FormId")Long formid){
        return formService.updateSelectForm(updateFormData,userid,formid);
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
