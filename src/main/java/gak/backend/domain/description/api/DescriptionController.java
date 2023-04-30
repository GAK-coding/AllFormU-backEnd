package gak.backend.domain.description.api;

import gak.backend.domain.description.application.DescriptionService;
import gak.backend.domain.description.dao.DescriptionRepository;
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
    public Optional<Description> getId(@PathVariable("id")Long id){
        return descriptionRepository.findById(id);
    }

    //응답자가 없을때 질문 수정(응답자가 있는지 없는지는 formService에서 확인..?)
    //응답자가 없다고치면 description에서는 퀴즈일 경우 퀴즈 답만 수정이 가능한겨
    //✔️✔️그러니까 주관식(description)에서 수정을 굳이 한다면 붙어야하는 가정들이
    //1. 해당 설문지의 응답자가 없어야함
    //2. 해당 퀴즈의 유형이 퀴즈 유형이라 설문 생성자가 퀴즈의 정답을 미리 지정했을때
    //-> 그 지정해두었던 answer를 수정하는 로직임 밑에가
    @PutMapping("/description/updateAnswer/{id}")
    public ResponseEntity<Description> updateDescriptionAnswer(@PathVariable(value = "id") Long id,
                                                               @RequestBody Map<String, String> requestBody) throws ResourceNotFoundException {
        Description description = descriptionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Description not found for this id : " + id));

        String answer = requestBody.get("answer");
        description.setAnswer(answer);
//      System.out.println("😍😍😍😍"+answer);
        final Description updatedDescription = descriptionRepository.save(description);
        return ResponseEntity.ok(updatedDescription);
    }
}
