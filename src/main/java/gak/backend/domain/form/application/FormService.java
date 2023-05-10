package gak.backend.domain.form.application;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.description.model.QDescription;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.exception.NotFoundFormException;
import gak.backend.domain.form.exception.NotFoundMemberException;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.form.model.QForm;
import gak.backend.domain.form.model.Separator;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.QMember;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.exception.NotFoundQuestionException;
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
    public Long createForm(FormDTO formDto, Long id) {

        //폼 엔티티 데이터 저장 (of메소드 호출)
        Form form=formDto.of();


        //member 연관관계 갖기위해 해당 id의 member 객체 가져옴
        Member author=memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException(id));
        form.AuthorSetting(author);
        form.SeparatorSetting(Separator.SEPARATOR_WRITER);

        //question리스트 저장 , que   stion엔티티 객체 자동 생성.
        List<Question> questions= formDto.toQuestions(form);
        form.QuestionSetting(questions);
        formRepository.save(form);

        return form.getId(); //question에서 여기서 생성 된 객체 사용하기 위해 id값 반환
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

        if (forms.isEmpty())
            throw new NotFoundFormException(id);
        else if(forms==null)
            throw new NotFoundFormException(id);

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

        if(form_sgl==null)
            throw new NotFoundFormException(id,FormId);


        return form_sgl;


    }

    //유저 상관없이 모든 설문 조회 필요하려나?
    public List<Form> getAllForms() {
        return formRepository.findAll();
    }


    /*
        특정 user의 특정 form을 업데이트
        (하위 객체(List) 제외)
    */
   @Transactional
  public FormDTO updateSelectForm(FormDTO formDto,Long Userid, Long FormId ) {

       Form form=getSelectFormById(Userid,FormId);
       form.UpdateSelectForm(formDto);

       return formDto;
  }



    /*
        userid의 모든 설문지 삭제
    */
    @Transactional
    public void deleteFormById(Long Userid){

        QForm form=QForm.form;

        QQuestion question = QQuestion.question;
        QDescription description = QDescription.description;
        QSelection selection = QSelection.selection;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);


        List<Form> forms=query.selectFrom(form)
                        .where(form.author.id.eq(Userid))
                        .fetch();

        if (forms==null)
            throw new NotFoundFormException(Userid);
        else if(forms.isEmpty())
            throw new NotFoundFormException(Userid);

        // 모든 Form에 속한 Question, Description, Selection을 모두 삭제
        forms.forEach(f->{
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
    public void deleteSelectFormById(Long Userid,Long FormId){

        QForm form = QForm.form;
        QQuestion question = QQuestion.question;
        QDescription description = QDescription.description;
        QSelection selection = QSelection.selection;

        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        List<Form> forms=query.selectFrom(form)
                .where(form.author.id.eq(Userid))
                .where(form.id.eq(FormId)) // 해당 userId의 해당 FormId를 조회
                .fetch();

        if (forms==null)
            throw new NotFoundFormException(Userid,FormId);
        else if(forms.isEmpty())
            throw new NotFoundFormException(Userid,FormId);

        // 해당 Form에 속한 Question, Description, Selection을 모두 삭제
        forms.forEach(f->{

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

    //id 상관없이 모든 데이터 삭제.
    @Transactional
    public void deleteForm(){

        formRepository.deleteAll();

    }
}