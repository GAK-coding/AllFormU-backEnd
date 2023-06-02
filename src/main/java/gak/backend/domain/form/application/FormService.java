package gak.backend.domain.form.application;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.description.model.QDescription;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.exception.NotFoundFormException;
import gak.backend.domain.form.exception.NotFoundMemberException;
import gak.backend.domain.form.model.Correspond;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.text.html.Option;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;

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

//    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @PersistenceContext
    private EntityManager entityManager;

    /*
        title, content까지 저장
        question 작성 후 리스트 완성 후 저장
    */
    @Transactional
    public Long createForm(FormDTO formDto, Long id){

        //폼 엔티티 데이터 저장 (of메소드 호출)
        Form form=formDto.of();

        List<String> datetime= formDto.getTimeout();
        int flag=0;

        //ExpireCorrespond(form,formDto);
        //member 연관관계 갖기위해 해당 id의 member 객체 가져옴
        Member author=memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException(id));
        form.AuthorSetting(author);
        form.SeparatorSetting(Separator.SEPARATOR_WRITER);
        form.CorrespondSetting(Correspond.STATUS_BEFORE);

        //question리스트 저장 , question엔티티 객체 자동 생성.
        List<Question> questions= formDto.toQuestions(form);
        form.QuestionSetting(questions);
        formRepository.save(form);

        //첫번 째 배열이 시작시간 두번 째가 만료시간
        for(String time : datetime){

            //현재 시간 받아옴
            Date now=Calendar.getInstance().getTime();
            Correspond status=Correspond.STATUS_BEFORE;
            //받은 시간 형식 설정
            DateFormat format=new SimpleDateFormat("yyyy MM dd HH mm ss");
            try {

                Date end = format.parse(time);
                //전달 받은 시간과 현재시간 차이 계산
                Long sec=(end.getTime() -now.getTime())/1000;


                //시작
                //만일 받아 온 시간보다 지나 있을 경우엔 0으로 초기화해서 바로 진행 중으로 바뀌게 함.

                if(sec<0 && flag==0){
                    status=Correspond.STATUS_PROCESS;
                    sec=1L;
                }
                //시작
                else if(sec>=0 && flag==0) {
                    status = Correspond.STATUS_PROCESS;

                }
                //만료
                if(flag==1){
                    status=Correspond.STATUS_EXPIRE;

                }
                //현재 form, 카운트시간, status
                ExpireCorrespond(form,sec,status);
            }
            catch(Exception e){
                e.printStackTrace();
            }
            //2개 배열.
            flag++;
        }



        return form.getId(); //question에서 여기서 생성 된 객체 사용하기 위해 id값 반환
    }
    @Transactional
    public void ExpireCorrespond(Form form, Long time, Correspond status){

            //스레드로 넘어가서 독립적으로 실행되게 함
            ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
            executor.schedule(() -> {
                form.CorrespondSetting(status);
                formRepository.save(form);
            }, time, TimeUnit.SECONDS);// 받은 시간이 만료 될때 위의 status가 변경.

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
  public Form updateSelectForm(FormDTO formDto,Long Userid, Long FormId ) {

       Form form=getSelectFormById(Userid,FormId);
       form.UpdateSelectForm(formDto);

       return form;
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