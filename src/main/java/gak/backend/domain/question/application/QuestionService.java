package gak.backend.domain.question.application;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.description.model.QDescription;
import gak.backend.domain.form.application.FormService;
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
import gak.backend.domain.question.model.QQuestion;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.model.QSelection;
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
    private final FormService formService;

        /*
            formcontroller에서 첫 form생성시 호출되는 service
            formDTO에서 question리스트를 가져와 포함 된 모든 하위List(selection..등..)
            를 question엔티티의 모든 하위 List에 저장.
        */
    @Transactional
    public Long createInit(FormDTO formDTO,Long id,Long FormId){


        List<Question> questions = questionRepository.findByFormId(FormId);
        List<QuestionDTO> questionDto=formDTO.getQuestions();
        if (questions.isEmpty())
            throw new NotFoundException("Question not found");


        List<Question> Questions=new ArrayList<>();
        for (Question question : questions) {
            if (!formDTO.getQuestions().isEmpty()) {
                for (QuestionDTO questionDTO : questionDto) {
                    if(question.getType()==Format.Description_SHORT)//수정 필요
                        question.DescriptionsSetting(questionDTO.toDescription(descriptionRepository, question));
                    else
                        question.OptionsSetting(questionDTO.toSelection(selectionRepository, question));
                }
                Questions.add(question);
            }
        }

            questionRepository.saveAll(Questions);
        return FormId;
    }

        /*
            form은 만들어져 있고 form 안에 모든 question이 제거 되었을 때
            question만 추가하는 작업 필요.
        */

    //원래 다른 도메인의 service를 사용하기도 하나?
//    @Transactional
//    public Question createQuestion(QuestionDTO questionDTO,Long FormId){
//        QForm qform = QForm.form;
//        JPAQueryFactory query = new JPAQueryFactory(entityManager);
//        Form form = query
//                .selectFrom(qform)
//                .where(qform.id.eq(FormId))
//                .fetchOne();
//
//        for(QuestionDTO questiondto:questionDTO){
//
//        }
//        Question question=questionDTO.of();
//        question.DescriptionsSetting(questionDTO.toDescription(descriptionRepository,question));
//        question.OptionsSetting(questionDTO.toSelection(selectionRepository,question));
//
//        List<Question> Questions=new ArrayList<>();
//        for (Question question : questions) {
//            if (!formDTO.getQuestions().isEmpty()) {
//                for (QuestionDTO questionDTO : questionDto) {
//                    if(question.getType()==Format.Description_SHORT)//수정 필요
//                        question.DescriptionsSetting(questionDTO.toDescription(descriptionRepository, question));
//                    else
//                        question.OptionsSetting(questionDTO.toSelection(selectionRepository, question));
//                }
//                Questions.add(question);
//            }
//        }
//        List<Question> questions=getAllQuestion(FormId,1L);
//        return question;
//    }


        /*
            formId에 해당하는 특정 question 의 value 수정
            (selection과 같은 하위 list 제외한)
        */
    @Transactional
    public QuestionDTO UpdateSelectQuestion(QuestionDTO questionDTO,Long FormId,Long QuestionId){

        Question question_List=getSelectQuestion(FormId,QuestionId);
        question_List.UpdateSelectQuestion(questionDTO);

        return questionDTO;


    }


        /*
            formId에 해당하는 모든 질문을 조회
        */
    @Transactional
    public List<Question> getAllQuestion(Long FormId, Long QuestionId){

        QQuestion qQuestion = QQuestion.question;
        QForm qForm=QForm.form;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        List<Question> question_sgl = query
                .selectFrom(qQuestion)
                .where(qForm.id.eq(FormId))
                .fetch();

        return question_sgl;
    }

        /*
            formId에 해당하는 특정 question 조회
        */
    @Transactional
    public Question getSelectQuestion( Long FormId,Long QuestionId){

        QQuestion qQuestion = QQuestion.question;
        QForm qForm=QForm.form;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        Question question_sgl = query
                .selectFrom(qQuestion)
                .where(qQuestion.question.id.eq(QuestionId)
                        .and(qForm.author.id.eq(FormId)))
                .fetchOne();

        return question_sgl;
    }

        /*
            formId에 해당하는 모든 question 삭제
            (selection과 같은 하위 list 포함한)
        */
    @Transactional
    public void DeleteAllQuestion(Long FormId){

        QQuestion question = QQuestion.question;
        QDescription description = QDescription.description;
        QSelection selection = QSelection.selection;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);

        query.selectFrom(question)
                .where(question.form.id.eq(FormId))
                .fetch()
                .forEach(f -> {
                    // Question에 속한 Description, Selection을 모두 삭제
                    query.delete(selection)
                            .where(selection.question.eq(f))
                            .execute();
                    query.delete(description)
                            .where(description.question.eq(f))
                            .execute();
                    query.delete(question)
                            .where(question.eq(f))
                            .execute();
                });
    }

        /*
            formId에 해당하는 특정 question 제거
            (selection과 같은 하위 list 포함한)
        */

    @Transactional
    public void DeleteSelectQuestion(Long FormId, Long QuestionId) {

        QQuestion question = QQuestion.question;
        QDescription description = QDescription.description;
        QSelection selection = QSelection.selection;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        query.selectFrom(question)
                .where(question.form.id.eq(FormId))
                .where(question.id.eq(QuestionId)) // 해당 userId의 해당 FormId를 조회
                .fetch()
                .forEach(f -> {
                    // 해당 Question에 속한Description, Selection을 모두 삭제
                    query.delete(selection)
                            .where(selection.question.eq(f))
                            .execute();
                    query.delete(description)
                            .where(description.question.eq(f))
                            .execute();
                    query.delete(question)
                            .where(question.eq(f))
                            .execute();
                });
    }
}
