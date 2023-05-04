package gak.backend.domain.form.application;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.form.model.QForm;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.QMember;
import gak.backend.domain.question.model.Question;
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

    @PersistenceContext
    private EntityManager entityManager;
    /*
        title, content까지 저장
        question 작성 후 리스트 완성 후 저장
    */
    @Transactional
    public Form createForm(FormDTO formDto,Long id) {
        Form form = new Form();
        form.setTitle(formDto.getTitle());
        form.setContent(formDto.getContent());

        Member author = memberRepository.findById(id).orElseThrow(() -> new RuntimeException("Member not found"));
        form.setAuthor(author);
        //form.setQuestions(formDto.getQuestions());
//        List<Question> questions = formDto.getQuestions().stream()
//                .map(questionDto -> {
//                    Question question = new Question();
//                    question.setContent(questionDto.getContent());
//                    question.setTitle(questionDto.getTitle());
//                    List<Selection> selections=questionDto.getSelections().stream()
//                            .map(selectionDto->{
//                                Selection selection=new Selection();
//                                selection.setContent(selectionDto.getContent());
//                                return selection;
//                            })
//                            .collect(Collectors.toList());
//                    question.setSelection(selections);
//                    List<Description> descriptions = questionDto.getDescriptions().stream()
//                            .map(descriptionDto -> {
//                                Description description = new Description();
//                                description.setContent(descriptionDto.getContent());
//                                return description;
//                            })
//                            .collect(Collectors.toList());
//                    question.setDescriptions(descriptions);
//                    return question;
//                })
//                .collect(Collectors.toList());



//        System.out.println("this: " + questions.get(0) + " ");
//        form.setQuestions(questions);
        return formRepository.save(form);
    }


    /*
        id 해당되는 설문지 조회
        해당되는 아이디 없으면 예외처리
    */
    public List<Form> getFormById(Long id) {

        QForm form=QForm.form;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);
        List<Form> forms=query
                .selectFrom(form)
                .where(form.author.id.eq(id))
                .fetch();

        return forms;
    }


    /*
        author id의 formid에 해당되는 설문지 조회.
    */
    public Form getSelectFormById(Long id,Long FormId) {

        QForm form=QForm.form;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);
        Form form_sgl=query
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
//        List<Form> form = getFormById(id);
//
//        form.setTitle(formDto.getTitle());
//        form.setContent(formDto.getContent());
//        List<Question> questions = formDto.getQuestions().stream()
//                .map(questionDto -> {
//                    Optional<Question> questionOptional = form.getQuestions().stream()
//                            .filter(question -> question.getId().equals(questionDto.getId()))
//                            .findFirst();
//
//                })
//                .collect(Collectors.toList());
//    }
//
//        form.setQuestions(questions);
//        return formRepository.save(form);
//    }

    /*
        userid의 모든 설문지 삭제
    */
    @Transactional
    public void deleteFormById(Long id){

        QForm form=QForm.form;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);

        query.delete(form)
                .where(form.author.id.eq(id))
                .execute();

    }


    /*
       user id의 formid에 해당되는 설문지 삭제
   */
    @Transactional
    public void deleteSelectFormById(Long id,Long FormId){

        QForm form=QForm.form;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);

        query.delete(form)
                .where(form.author.id.eq(id)
                        .and(form.id.eq(FormId)))
                .execute();

    }

    //id 상관없이 모든 데이터 삭제.
    @Transactional
    public void deleteForm(){

        formRepository.deleteAll();

    }
}