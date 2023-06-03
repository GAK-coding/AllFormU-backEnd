package gak.backend.domain.form.application;


import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.model.QDescription;
import gak.backend.domain.file.application.FileService;
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
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.model.QQuestion;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.response.model.QResponse;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.model.QSelection;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;

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
    private final FileService fileService;



    @PersistenceContext
    private EntityManager entityManager;




    @Transactional
    public boolean fixable(Long FormId){

        JPAQueryFactory query=new JPAQueryFactory(entityManager);
        QForm form=QForm.form;
        QQuestion question=QQuestion.question;
        QResponse Response=QResponse.response;


        Form form_sgl = query
                .selectFrom(form)
                .where(form.id.eq(FormId))
                .fetchOne();


        if(form_sgl==null)
            throw new NotFoundFormException("Not found Form Id");
        /*
        수정 버튼을 누르면 해당 FormId를 받아옴
        그리고 response 테이블을 조회하고 각각의 questionId에 대응하는 form을 조회하고
        그 form의 id와 수정 버튼이 눌린 form의 id가 일치하면 응답이 하나라도 있는 것
        그렇게 되면 작성자는 수정이 불가능 하기에 해당 id를 가진 form의 fix값을 false로 수정.
        * */
        List<Long> form_id = query
                .select(Response.responsor.id)
                .from(Response)
                .join(Response.question,question)
                .join(question.form,form)
                .where(form.id.eq(FormId))
                .fetch();

        for(Long id:form_id)
            System.out.println("Formid: "+id);
        if(form_id.isEmpty()){
            return form_sgl.FixSetting(true);
        }




        return form_sgl.FixSetting(false);
    }
    @Transactional
    public FormDTO.PagingDTO Paging(Long UserId,Long page){

        JPAQueryFactory query=new JPAQueryFactory(entityManager);
        QForm form=QForm.form;
        QQuestion question=QQuestion.question;
        QResponse Response=QResponse.response;

        Long pageSize = 5L; // 페이지 당 데이터 개수
        Long startPage = page * pageSize; // 시작 페이지 번호

        Long totalDataCount = query
                .selectFrom(form)
                .where(form.author.id.eq(UserId))
                .fetchCount();
        System.out.println("total:"+totalDataCount);
        if (startPage >= totalDataCount) {
            //데이터가 없는 거임
        }
        Long nextPage = startPage + pageSize;

        boolean hasNextPage = nextPage < totalDataCount;

        List<Form> forms = query
                .selectFrom(form)
                .where(form.author.id.eq(UserId))
                .orderBy(form.id.desc())
                .limit(pageSize)
                .offset(startPage)
                .fetch();


        int response_cnt=0;
        for(Form form_temp : forms){

            List<Long> form_lst = query
                    .select(Response.responsor.id)
                    .from(Response)
                    .join(Response.question,question)
                    .join(question.form,form)
                    .where(Response.question.form.id.eq(form_temp.getId()))
                    .distinct()
                    .fetch();

            response_cnt=form_lst.size();
            form_temp.ResponsorCntSetting(response_cnt);
            response_cnt=0;
        }





        List<FormDTO.PagingDataDTO> pagingDTOList = forms.stream()
                .map(Form::toPagingData)
                .collect(Collectors.toList());

        FormDTO.PagingDTO paging=new FormDTO.PagingDTO(hasNextPage,pagingDTOList);

        return paging;
    }





    /*
        title, content까지 저장
        question 작성 후 리스트 완성 후 저장
    */
    @Transactional
    public Long createForm(FormDTO.AllFormData allFormData, Long id){

        //폼 엔티티 데이터 저장 (of메소드 호출)
        Form form=allFormData.of();

        List<String> datetime= allFormData.getTimeout();
        int flag=0;

        //ExpireCorrespond(form,formDto);
        //member 연관관계 갖기위해 해당 id의 member 객체 가져옴
        Member author=memberRepository.findById(id).orElseThrow(() -> new NotFoundMemberException(id));
        form.AuthorSetting(author);
        form.SeparatorSetting(Separator.SEPARATOR_WRITER);
        form.CorrespondSetting(Correspond.STATUS_BEFORE);
        form.TimeoutSetting(datetime);
        //question리스트 저장 , question엔티티 객체 자동 생성.
        List<Question> questions= allFormData.toQuestions(form);
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
  public Form updateSelectForm(FormDTO.UpdateFormData updateFormData, Long Userid, Long FormId ) {

        //userid가 작성한 폼중에 해당 formid가져오기
        Form form=getSelectFormById(Userid,FormId);
        //기존의 폼이미지 값이 있다면 기존값 s3에서 삭제 후 다시 업로드
        if(form.getFimage()!=null){
            fileService.deleteImageByUrl(form.getFimage());
        }
        form.UpdateSelectForm(updateFormData);

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