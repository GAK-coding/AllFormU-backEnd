package gak.backend.domain.description.application;

import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.question.dto.QuestionDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DescriptionService {
    private final DescriptionRepository descriptionRepository;


    @Transactional
    public Description createInit(FormDTO formDTO){
        Description description=new Description();

        for (QuestionDTO question : formDTO.getQuestions()) {
            // 질문의 첫번째 설명을 저장
            if(!question.getDescriptions().isEmpty()) {
                DescriptionDTO descriptionDTO = question.getDescriptions().get(0);

                description.setContent(descriptionDTO.getContent());


            }
        }
        System.out.println("Description:"+description.getContent());
        //description.create(descriptionDTO.getAnswer(),descriptionDTO.getQuiz(),descriptionDTO.getContent());
        return descriptionRepository.save(description);
    }
    //description 생성
    @Transactional
    public Description createDescription(DescriptionDTO descriptionDTO){
        Description description=new Description();
        description.create(descriptionDTO.getAnswer(),descriptionDTO.getQuiz(),descriptionDTO.getContent());
        return descriptionRepository.save(description);
    }

    //descriptionid로 해당 description 조회
    public Description getDescription(Long id){
        return descriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Description not found with id: " + id));
    }

    //question_id로 해당 description 조회
    public List <Description> getDescriptionByQ(Long quesion_id){
        return descriptionRepository.findByQuestionId(quesion_id);

    }


    //응답자가 없을때 질문 수정(응답자가 있는지 없는지는 formService에서 확인..?)
    //응답자가 없다고치면 description에서는 퀴즈일 경우 퀴즈 답만 수정이 가능한겨
    //✔️✔️그러니까 주관식(description)에서 수정을 굳이 한다면 붙어야하는 가정들이
    //1. 해당 설문지의 응답자가 없어야함
    //2. 해당 퀴즈의 유형이 퀴즈 유형이라 설문 생성자가 퀴즈의 정답을 미리 지정했을때
    //-> 그 지정해두었던 answer를 수정하는 로직임 밑에가
    //description 수정
    @Transactional
    public Description updateDescription(Long id, String newAnswer){
        Description description=getDescription(id);
        description.update(newAnswer);
        return descriptionRepository.save(description);
    }

    @Transactional
    public void deleteSelectionById(Long id){

        Optional<Description> optionalValue = descriptionRepository.findById(id);
        if (!optionalValue.isPresent()) {
            throw new RuntimeException("Form is not present");
        }
        descriptionRepository.deleteById(id);

    }







}
