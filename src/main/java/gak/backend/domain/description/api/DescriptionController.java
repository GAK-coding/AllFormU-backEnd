package gak.backend.domain.description.api;

import gak.backend.domain.description.application.DescriptionService;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.model.Description;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    DescriptionRepository descriptionRepository;
    @Autowired
    private final DescriptionService descriptionService;

    //content- 답변자의 정답
    //quiz가 true일때 생성자가 answer에 답을 지정해줘야함
    //quiz가 false일때 answer은 null

    //description생성
    @PostMapping("/description/createDescription")
    public String add(@RequestBody Description description){
        descriptionRepository.save(description);
        return "create";
    }

    //description조회
    @GetMapping("/description/getDescription/{id}")
    public Description getId(@PathVariable("id")Long id){
        return descriptionService.getDescription(id);
    }

    //quesstion_id로 description조회
    @GetMapping("/description/getDescriptionByQ/{question_id}")
    public Optional<Description> getIdByQ(@PathVariable("question_id")Long question_id){
        return descriptionService.getDescriptionByQ(question_id);
    }


    @PutMapping("/description/updateAnswer/{id}")
    public ResponseEntity<Description> updateDescriptionAnswer(@PathVariable(value = "id") Long id,
                                                               @RequestBody Map<String, String> requestBody){

        String answer = requestBody.get("answer");
        final Description updatedDescription=descriptionService.updateDescription(id,answer);
        return ResponseEntity.ok(updatedDescription);
    }
}
