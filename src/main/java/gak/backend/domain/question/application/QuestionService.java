package gak.backend.domain.question.application;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.form.model.QForm;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.question.exception.NotFoundException;
import gak.backend.domain.question.model.Format;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.dao.SelectionRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class QuestionService {

    @PersistenceContext
    private EntityManager entityManager;
    private final MemberRepository memberRepository;
    private final SelectionRepository selectionRepository;
    private final DescriptionRepository descriptionRepository;
    private final FormRepository formRepository;
    private final QuestionRepository questionRepository;
    @Transactional
    public List<Question> createInit(FormDTO formDTO,Long id,Long FormId){


        List<Question> questions = questionRepository.findByFormId(FormId);
        List<QuestionDTO> questionDto=formDTO.getQuestions();
        if (questions.isEmpty())
            throw new NotFoundException("Question not found");


        List<Question> Questions=new ArrayList<>();
        for (Question question : questions) {
            if (!formDTO.getQuestions().isEmpty()) {
                for (QuestionDTO questionDTO : questionDto) {
                    if(question.getType()==Format.Description_SHORT)
                        question.DescriptionsSetting(questionDTO.toDescription(descriptionRepository, question));
                    else
                        question.OptionsSetting(questionDTO.toSelection(selectionRepository, question));
                }
                Questions.add(question);
            }
        }


        return questionRepository.saveAll(Questions);
    }
}
