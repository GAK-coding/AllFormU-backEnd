package gak.backend.domain.form.application;

import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/*
멤버 서비스 로직
C(생성):
    1.
R(조회):
    1.
U(업데이트)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;

    /*
        title, content까지 저장
        question 작성 후 리스트 완성 후 저장
    */
    @Transactional
    public Form createForm(FormDTO formDto) {
        Form form = new Form();
        form.setTitle(formDto.getTitle());
        form.setContent(formDto.getContent());

        List<Question> questions = formDto.getQuestions().stream()
                .map(questionDto -> {
                    Question question = new Question();
                    //question.setContent(questionDto.getContent());
                    return question;
                })
                .collect(Collectors.toList());


        System.out.println("this: " + questions.get(0) + " ");
        form.setQuestions(questions);
        return formRepository.save(form);
    }


    /*
        id 해당되는 설문지 조회
        해당되는 아이디 없으면 예외처리
    */
    public Form getFormById(Long id) {
        return formRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + id));

    }

    public List<Form> getAllForms() {
        return formRepository.findAll();
    }


    /*
        업데이트
    */
    @Transactional
    public Form updateForm(Long id, FormDTO formDto) {
        Form form = getFormById(id);

        form.setTitle(formDto.getTitle());
        form.setContent(formDto.getContent());
//        List<Question> questions = formDto.getQuestions().stream()
//                .map(questionDto -> {
//                    Optional<Question> questionOptional = form.getQuestions().stream()
//                            .filter(question -> question.getId().equals(questionDto.getId()))
//                            .findFirst();
//
//                })
//                .collect(Collectors.toList());
//    }

        //form.setQuestions(questions);
        return formRepository.save(form);
    }

    /*
        id 해당되는 설문지 삭제
        해당되는 아이디 없으면 예외처리
    */
    @Transactional
    public void deleteFormById(Long id){

        Optional<Form> optionalValue = formRepository.findById(id);
        if (!optionalValue.isPresent()) {
            throw new RuntimeException("Form is not present");
        }
        formRepository.deleteById(id);

    }
    @Transactional
    public void deleteForm(){

        formRepository.deleteAll();

    }
}