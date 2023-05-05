package gak.backend.domain.form.application;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.description.model.QDescription;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.form.model.QForm;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.QMember;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.model.QQuestion;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.model.QSelection;
import gak.backend.domain.selection.model.Selection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mysema.commons.lang.Assert.assertThat;

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
    private final MemberRepository memberRepository;
    private final SelectionRepository selectionRepository;
    private final QuestionRepository questionRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /*
        title, content까지 저장
        question 작성 후 리스트 완성 후 저장
    */
    @Transactional
    public Form createForm(FormDTO formDto, Long id) {

        /*
            테스트 위해서 작성
        */
        //폼 엔티티 데이터 저장
        Form form = new Form();

        //member엔티티와 form엔티티 연결 ->member_id
        Member author = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        form.setAuthor(author); //setter를 아예 안 쓸수는 없음. 최대한 이뮤터블 객체로 만들어야함
        form.setStatus(formDto.getStatus());
        //question 생성, 데이터 삽입 후 question리스트 생성
        List<Question> questions = formDto.getQuestions().stream()
                .map(questionDto -> {
                    Question question = new Question();
                    question.setContent(questionDto.getContent());
                    question.setTitle(questionDto.getTitle());
                    question.setType(questionDto.getType());

                    //selection객체 생성, 데이터 삽입 후 selection리스트 생성
                    List<Selection> selections = questionDto.getOptions().stream()
                            .map(selectionDto -> {
                                Selection selection = new Selection();
                                selection.setContent(selectionDto.getContent());
                                selection.setQuestion(question);
                                return selection;
                            })
                            .collect(Collectors.toList());

                    //만들어진 selections 리스트 question의 selection컬럼에 삽입
                    question.setOptions(selections);

                    //description객체 생성, 데이터 삽입 후 description리스트 생성
                    List<Description> descriptions = questionDto.getDescriptions().stream()
                            .map(descriptionDto -> {
                                Description description = new Description();
                                description.setContent(descriptionDto.getContent());
                                description.setTitle(descriptionDto.getTitle());
                                description.setQuestion(question);
                                return description;
                            })
                            .collect(Collectors.toList());

                    //만들어진 descriptions 리스트 question의 descriptions컬럼에 삽입
                    question.setDescriptions(descriptions);
                    question.setForm(form);
                    return question;
                })
                .collect(Collectors.toList());

        //만들어진 questions 리스트 form의 questions컬럼에 삽입
        form.setQuestions(questions);
        return formRepository.save(form);
    }


    /*
        id 해당되는 설문지 조회
        해당되는 아이디 없으면 예외처리
    */
    public List<Form> getFormById(Long id) {

        QForm form = QForm.form;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        List<Form> forms = query
                .selectFrom(form)
                .where(form.author.id.eq(id))
                .fetch();
        /////////////////////
        return forms;
    }


    /*
        author id의 formid에 해당되는 설문지 조회.
    */
    public Form getSelectFormById(Long id, Long FormId) {

        QForm form = QForm.form;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);
        Form form_sgl = query
                .selectFrom(form)
                .where(form.author.id.eq(id)
                        .and(form.id.eq(FormId)))
                .fetchOne();

        return form_sgl;


    }

    //유저 상관없이 모든 설문 조회 필요하려나?
    public List<Form> getAllForms() {
        return formRepository.findAll();
    }


    /*
        업데이트
    */
//    @Transactional
//    public Form updateForm(Long id, FormDTO formDto) {
//        Form form = formRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + id));
//
//        form.setTitle(formDto.getTitle());
//        form.setContent(formDto.getContent());
//        List<Question> questions = formDto.getQuestions().stream()
//                .map(questionDto -> {
//                    Optional<Question> questionOptional = form.getQuestions().stream()
//                            .filter(question -> question.getId().equals(questionDto.getId()))
//                            .findFirst();
//
//
//                    if (questionOptional.isPresent()) {
//                        Question question = questionOptional.get();
//                        question.setContent(questionDto.getContent());
//
//                        List<Selection> selections = questionDto.getOptions().stream()
//                                .map(selectionDto -> {
//                                    Optional<Selection> selectionOptional = question.getOptions().stream()
//                                            .filter(selection -> selection.getId().equals(selectionDto.getId()))
//                                            .findFirst();
//
//                                    if (selectionOptional.isPresent()) {
//                                        Selection option = selectionOptional.get();
//                                        option.setContent(selectionDto.getContent());
//                                        return option;
//                                    } else {
//                                        Selection option = new Selection();
//                                        option.setContent(selectionDto.getContent());
//                                        option.setQuestion(question);
//                                        return selectionRepository.save(option);
//                                    }
//                                })
//                                .collect(Collectors.toList());
//                        question.setOptions(selections);
//                        return question;
//                    } else {
//                        Question question = new Question();
//                        question.setTitle(questionDto.getTitle());
//                        question.setContent(questionDto.getContent());
//                        question.setForm(form);
//                        //question.set(questionDto.getSectionNum());
//                        //question.setType(questionDto.getType());
//                        //question.setRequired(questionDto.isRequired());
//
//                        List<Selection> options = questionDto.getOptions().stream()
//                                .map(selectionDto -> {
//                                    Selection option = new Selection();
//                                    option.setContent(selectionDto.getContent());
//                                    option.setQuestion(question);
//                                    return selectionRepository.save(option);
//                                })
//                                .collect(Collectors.toList());
//
//                        question.setOptions(options);
//                        return questionRepository.save(question);
//                    }
//                })
//                .collect(Collectors.toList());
//        form.setQuestions(questions);
//        return formRepository.save(form);
//    }



    /*
        userid의 모든 설문지 삭제
    */
    @Transactional
    public void deleteFormById(Long id){

        QForm form=QForm.form;

        QQuestion question = QQuestion.question;
        QDescription description = QDescription.description;
        QSelection selection = QSelection.selection;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);

        query.selectFrom(form)
                .where(form.author.id.eq(id))
                .fetch()
                .forEach(f -> {
                    // Form에 속한 Question, Description, Selection을 모두 삭제
                    query.delete(selection)
                            .where(selection.question.in(
                                    JPAExpressions.selectFrom(question)
                                            .where(question.form.eq(f))
                                            .select(question)
                            ))
                            .execute();
                    query.delete(description)
                            .where(description.question.in(
                                    JPAExpressions.selectFrom(question)
                                            .where(question.form.eq(f))
                                            .select(question)
                            ))
                            .execute();
                    query.delete(question)
                            .where(question.form.eq(f))
                            .execute();
                    // Form을 삭제
                    query.delete(form)
                            .where(form.eq(f))
                            .execute();
                });
    }


    /*
       user id의 formid에 해당되는 설문지 삭제
   */
    @Transactional
    public void deleteSelectFormById(Long id,Long FormId){

        QForm form = QForm.form;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

//        query.delete(QSelection.selection)
//                .where(QSelection.selection.question.in(
//                        JPAExpressions.select(QQuestion.question.id)
//                                .from(QQuestion.question)
//                                .where(QQuestion.question.form.id.eq(FormId))))
//                .execute();
//
//        query.delete(QDescription.description)
//                .where(QDescription.description.question.in(
//                        JPAExpressions.select(QQuestion.question.id)
//                                .from(QQuestion.question)
//                                .where(QQuestion.question.form.id.eq(FormId))))
//                .execute();
//
//        query.delete(QQuestion.question)
//                .where(QQuestion.question.form.id.eq(FormId))
//                .execute();
//
//        query.delete(form)
//                .where(form.id.eq(FormId).and(form.author.id.eq(id)))
//                .execute();

    }

    //id 상관없이 모든 데이터 삭제.
    @Transactional
    public void deleteForm(){

        formRepository.deleteAll();

    }
}