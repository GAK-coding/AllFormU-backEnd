package gak.backend.domain.question.application;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.model.QDescription;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.form.model.QForm;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.question.exception.NotFoundQuestionException;
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

import java.util.ArrayList;
import java.util.List;

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

        if (questions==null)
            throw new NotFoundQuestionException("Question not found");
        else if(questions.isEmpty())
            throw new NotFoundQuestionException("Question not found");

        List<Question> Questions=new ArrayList<>();
        for (Question question : questions) {
            if (!formDTO.getQuestions().isEmpty()) {
                for (QuestionDTO questionDTO : questionDto) {
                    if(question.getType().name().startsWith("Description"))//수정 필요
                        question.DescriptionsSetting(questionDTO.toDescription(descriptionRepository, question));
                    else
                        question.OptionsSetting(questionDTO.toSelection(selectionRepository, question));
                }
                Questions.add(question);
            }
        }
        System.out.println("여기까지 오니?");

            questionRepository.saveAll(Questions);
        return FormId;
    }

        /*
            리스트로 question데이터를 받아와서 기존 form에 추가해주는 프로세스

        */

    //원래 다른 도메인의 service를 사용하기도 하나?
    @Transactional
    public List<Long> createQuestion(List<QuestionDTO> questionDTO,Long FormId){
        QForm qform = QForm.form;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        Form form = query
                .selectFrom(qform)
                .where(qform.id.eq(FormId))
                .fetchOne();



        if(form == null){
            throw new NotFoundQuestionException("Form not found");
        }

        List<Question> question_list=form.getQuestions();
        //List<Question> question_list=new ArrayList<>();
        List<Long> QuestionID=new ArrayList<>();

        //받은 question 데이터를 엔티티에 저장.
        for(QuestionDTO temp_question : questionDTO){
            Question question=temp_question.of(form);

            if(temp_question.getType().name().startsWith("Description"))
                question.DescriptionsSetting(temp_question.toDescription(descriptionRepository,question));
            else
                question.OptionsSetting(temp_question.toSelection(selectionRepository, question));

            question_list.add(question);
        }
        List<Question> saveQuestion=questionRepository.saveAll(question_list);
        //save이후에 pk값을 받아올 수 있음.
        for(Question savedQuestion : saveQuestion) {
            Long id = savedQuestion.getId();
            QuestionID.add(id);
            System.out.println("QuestionId:" + id);
        }

        return QuestionID;
    }


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

        if(question_sgl==null){
            throw new NotFoundQuestionException(FormId);
        }
        else if(question_sgl.isEmpty()){
            throw new NotFoundQuestionException(FormId);
        }
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

        if(question_sgl==null){
            throw new NotFoundQuestionException(FormId,QuestionId);
        }

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

        List<Question> questions=query.selectFrom(question)
                .where(question.form.id.eq(FormId))
                .fetch();

        if(questions==null){
            throw new NotFoundQuestionException(FormId);
        }
        else if(questions.isEmpty()){
            throw new NotFoundQuestionException(FormId);
        }

        // Question에 속한 Description, Selection을 모두 삭제
        questions.forEach(f->{
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


        List<Question> questions=query.selectFrom(question)
                .where(question.form.id.eq(FormId))
                .where(question.id.eq(QuestionId))
                .fetch();

        if(questions==null){
            throw new NotFoundQuestionException(FormId,QuestionId);
        }
        else if(questions.isEmpty()){
            throw new NotFoundQuestionException(FormId);
        }


        // 해당 Question에 속한Description, Selection을 모두 삭제
        questions.forEach(f->{
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
