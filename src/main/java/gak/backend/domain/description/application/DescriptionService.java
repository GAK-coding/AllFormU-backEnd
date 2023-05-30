package gak.backend.domain.description.application;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.exception.NotFoundDescriptionException;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.description.model.QDescription;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Role;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.question.exception.NotFoundQuestionException;
import gak.backend.domain.question.model.QQuestion;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.exception.NotFoundSelectionException;
import gak.backend.domain.selection.model.QSelection;
import gak.backend.domain.selection.model.Selection;
import gak.backend.global.error.exception.NotFoundByIdException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static gak.backend.domain.description.dto.DescriptionDTO.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DescriptionService {
    //@PersistenceContext
    private EntityManager entityManager;
    private final DescriptionRepository descriptionRepository;
    private final QuestionRepository questionRepository;
    private final FormRepository formRepository;
    private final MemberRepository memberRepository;


//    @Transactional
//    public Description createInit(FormDTO formDTO){
//        Description description=new Description();
//
//        for (QuestionDTO question : formDTO.getQuestions()) {
//
//            if(!question.getDescriptions().isEmpty()) {
//                DescriptionDTO descriptionDTO = question.getDescriptions().get(0);
//
//                description.setContent(descriptionDTO.getContent());
//
//
//            }
//        }
//        System.out.println("Description:"+description.getContent());
//        //description.create(descriptionDTO.getAnswer(),descriptionDTO.getQuiz(),descriptionDTO.getContent());
//        return descriptionRepository.save(description);
//    }
    //====================================description 생성================================
    @Transactional
    public Long createDescription(DescriptionSaveRequest descriptionSaveRequest,Long QuestionId){
        //description은 응답자와 생성자로 나뉘기 때문에 form의 memberId와 똑같으면 멤버 구분 해놓고 정답을 처리해야되는 column으로 박아야할지 아니면 돌아가면서 찾아야할지
        //그럼 이론상 두번 돌아가는 거라서 좀 그렇다.
        //근데 question에서 질문의 형식으로 description을 갖고 있는데 이건 응답도 갖고 있는거니까 question이랑 떼어놔야할것같음.
        //member가 작성자인 동시에 응답자일수도 있기 대문에 STATUS로 상태를 비교하는 건 안좋은 것 같음.
        Question question = questionRepository.findById(QuestionId).orElseThrow(NotFoundByIdException::new);
        Member member = memberRepository.findById(descriptionSaveRequest.getMember_id()).orElseThrow(NotFoundByIdException::new);
        Form form = formRepository.findById(question.getForm().getId()).orElseThrow(NotFoundByIdException::new);
        Member author = memberRepository.findById(form.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);

        //응답자면 멤버 상태 변경
        if(member.getId() != author.getId()){
            member.UpdateMemberRole(Role.Role_Responsor);
        }



        QQuestion qQuestion=QQuestion.question;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);

        Question question_sgl = query
                .selectFrom(qQuestion)
                .where(qQuestion.question.id.eq(QuestionId))
                .fetchOne();

        if(question_sgl==null){
            throw new NotFoundDescriptionException(QuestionId);
        }

        Description description=descriptionSaveRequest.of(question_sgl);
        Description saveDescription=descriptionRepository.save(description);
        Long DescriptionId=saveDescription.getId();

        return DescriptionId;
    }

    //========================read============================================

    //descriptionid로 해당 description 조회
    @Transactional(readOnly = true)
    public Description getDescription(Long id){
        return descriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Description not found with id: " + id));
    }

    //question_id로 해당 description 조회 -> responsor, content만 나오게
    @Transactional(readOnly = true)
    public List <DescriptionSimpleInfoDTO> getDescriptionByQuestionId(Long question_id){
        List<Description> dList = descriptionRepository.findByQuestionId(question_id);
        List<DescriptionSimpleInfoDTO> dsList = new ArrayList<>();
        for(Description d : dList){
            dsList.add(DescriptionSimpleInfoDTO.builder()
                    .member_id(d.getMember().getId())
                    .content(d.getContent())
                    .build());
        }
        return dsList;

    }


    //description은 생성과 응답이 동시에 일어나니까 응답수를 셀때는 -1을 해야함.
    @Transactional(readOnly=true)
    public int countDescriptionsByQuestionId(Long questionId){
        int responseCnt = descriptionRepository.countDescriptionByQuestionId(questionId)-1;
        return responseCnt;
    }


    //퀴즈 정답자 출력
    //로직이 좀 성능적으로도 별로긴 한데 우선 구현. 나중에 리팩토링
    //우선 작성자가 아닌 사람의 답변은 문자열 비교를 통해 배열에 넣어둠. 배열에 넣어둔 것을 출력하는게 나을 것 같음. -> 이렇게 되면 나쁘지 않은 로직.
    //정답자를 알아도 사실은 한번은 돌아감. -> 메모리를 줄이냐의 차이?
    @Transactional(readOnly = true)
    public List<DescriptionSimpleInfoDTO> findQuizRightDescriptions(Long questionId){
        Question question = questionRepository.findById(questionId).orElseThrow(NotFoundByIdException::new);
        Form form = formRepository.findById(question.getForm().getId()).orElseThrow(NotFoundByIdException::new);
        Member author = memberRepository.findById(form.getAuthor().getId()).orElseThrow(NotFoundByIdException::new);
        List<Description> descriptions = descriptionRepository.findByQuestionId(questionId);
        String answer = ""; //주관식 퀴즈의 정답이 저장되어있음.
        List<String> responseOp = new ArrayList<String>(); //사람들이 응답한 주관식의 형태들이 보기 형식으로 담김
        List<List<Description>> response = new ArrayList<List<Description>>(); //이제 각각의 인덱스에 담김 .
        //정답 값들  SimpleInfo로 바꿈.
        List<DescriptionSimpleInfoDTO> sdList = new ArrayList<>();

       for(Description description : descriptions){
           //우선 돌면서 정답자를 찾아서 정답 입력
           if(description.getMember().getId() == author.getId()){
               answer = description.getContent();
               continue;
           }
           else{
               //정답 옵션과 비교 해야함. 만약에 같은 항목이 없다면 새로 추가하고 해당인덱스에 맞게 response에도 추가되어야함.
               //TODO response 목록이 없을 때 null예외가 터지면 추가해주기
//               if(responseOp.isEmpty()){
//                   responseOp.add(description.getContent());
//               }
               //TODO 사용자들이 입력한 주관식들의 보기를 돌고 돌아서 확인. -> 오히려 나중에 구글보다 정교하게 답안 처리 가능할듯.
               for(int i=0 ; i < responseOp.size(); i++){
                   //responseOp에 새로들어온 응답의 보기가 있다면 넣어주기
                   int num = responseOp.indexOf(description.getContent());
                   if( num > 0){ //-1이면 없다는 얘기 있으면 해당 인덱스를 넘겨줌.
                       response.get(num).add(description);
                   }
                   else{
                       //없으면 추가
                       responseOp.add(description.getContent());
                       response.get(responseOp.size()).add(description);
                   }
               }
               //정답으로 분류된 목록을 simpleInfoDTO로 바꾸고 출력.
               int answerIndex = responseOp.indexOf(answer);
               //for문 편하게 돌리려고 그냥 정답 리스트들 옮김.-> 여기서는 정답자들만 simpleDTO로 변환
               List<Description> rigthResponses = response.get(answerIndex);
               for(Description rd : rigthResponses){
                   sdList.add(rd.toDescriptionSimpleInfoDTO(rd.getMember()));
               }
           }
       }
        return sdList;

    }


    //각 항목별 분포
    //퀴즈에서도 이거 사용하면 그 항목들과 유사한애들 다 정답처리할 수 있음.
    //TODO 나중에 일괄 정답 처리하는 로직도 만들기 -> 고도화 -> 그러러면 컬럼이 필요하다 정답자인지 아닌지!!
    //TODO query로 날릴 수 있다면 생각해보기
    @Transactional(readOnly = true)
    public DescriptionStatisticDTO statistic(Long questionId){
        Question question = questionRepository.findById(questionId).orElseThrow(NotFoundByIdException::new);
        List<Description> descriptions = descriptionRepository.findByQuestionId(questionId);
        List<String> descriptionOp = new ArrayList<>(); //옵션 저장
        List<List<DescriptionSimpleInfoDTO>> dsList = new ArrayList<>();

        for(Description ds : descriptions){
            for(String option : descriptionOp){
                int num = descriptions.indexOf(option);
                if(num > 0 ) {//항목에 있음.
                    dsList.get(num).add(ds.toDescriptionSimpleInfoDTO(ds.getMember()));
                }
                else{//항목에 없음
                    descriptionOp.add(option);
                    dsList.get(descriptionOp.size()).add(ds.toDescriptionSimpleInfoDTO(ds.getMember()));
                }
            }
        }
        //size 각각 배열에 넣기
        int[] nums = new int[descriptionOp.size()];
        for(int i = 0; i < descriptionOp.size(); i++){
            nums[i] = dsList.get(i).size();
        }
        return DescriptionStatisticDTO.builder()
                .response(dsList)
                .num(nums)
                .build();
    }

    //questionId로 description 응답수 조회





//===================Update=================================

    //응답자가 없을때 질문 수정(응답자가 있는지 없는지는 formService에서 확인..?)
    //응답자가 없다고치면 description에서는 퀴즈일 경우 퀴즈 답만 수정이 가능한겨
    //✔️✔️그러니까 주관식(description)에서 수정을 굳이 한다면 붙어야하는 가정들이
    //1. 해당 설문지의 응답자가 없어야함
    //2. 해당 퀴즈의 유형이 퀴즈 유형이라 설문 생성자가 퀴즈의 정답을 미리 지정했을때
    //-> 그 지정해두었던 answer를 수정하는 로직임 밑에가
    //description 수정
    @Transactional
    public Description updateDescription(Long QuestionId,Long DescriptionId, String newContent){
        QDescription qDescription=QDescription.description;
        QQuestion qQuestion=QQuestion.question;
        JPAQueryFactory query = new JPAQueryFactory(entityManager);

        Description description_sgl = query
                .selectFrom(qDescription)
                .where(qDescription.id.eq(DescriptionId)
                        .and(qQuestion.id.eq(QuestionId)))
                .fetchOne();

        if(description_sgl==null){
            throw new NotFoundDescriptionException(QuestionId,DescriptionId);
        }

        description_sgl.updateContent(newContent);
        return description_sgl;
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
