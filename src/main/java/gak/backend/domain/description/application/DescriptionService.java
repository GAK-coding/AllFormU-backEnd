package gak.backend.domain.description.application;

import com.querydsl.jpa.impl.JPAQueryFactory;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.exception.NotFoundDescriptionException;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.description.model.QDescription;
import gak.backend.domain.form.dto.FormDTO;
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
    public Long createDescription(DescriptionDTO descriptionDTO,Long QuestionId){
        //description은 응답자와 생성자로 나뉘기 때문에 form의 memberId와 똑같으면 멤버 구분 해놓고 정답을 처리해야되는 column으로 박아야할지 아니면 돌아가면서 찾아야할지
        //그럼 이론상 두번 돌아가는 거라서 좀 그렇다.
        //근데 question에서 질문의 형식으로 description을 갖고 있는데 이건 응답도 갖고 있는거니까 question이랑 떼어놔야할것같음.

        QQuestion qQuestion=QQuestion.question;
        JPAQueryFactory query=new JPAQueryFactory(entityManager);

        Question question_sgl = query
                .selectFrom(qQuestion)
                .where(qQuestion.question.id.eq(QuestionId))
                .fetchOne();

        if(question_sgl==null){
            throw new NotFoundDescriptionException(QuestionId);
        }

        Description description=descriptionDTO.of(question_sgl);
        Description saveDescription=descriptionRepository.save(description);
        Long DescriptionId=saveDescription.getId();

        return DescriptionId;
    }

    //========================read============================================

    //descriptionid로 해당 description 조회
    public Description getDescription(Long id){
        return descriptionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Description not found with id: " + id));
    }

    //question_id로 해당 description 조회
    @Transactional(readOnly = true)
    public List <Description> getDescriptionByQ(Long question_id){
        return descriptionRepository.findByQuestionId(question_id);

    }

    //description은 생성과 응답이 동시에 일어나니까 응답수를 셀때는 -1을 해야함.

    @Transactional(readOnly=true)
    public int countDescriptionsByQuestionId(Long questionId){
        int responseCnt = descriptionRepository.countDescriptionByQuestionId(questionId)-1;
        return responseCnt;
    }


    //퀴즈 정답자 출력
//    @Transactional//(reaOnly=true)
//    public List<DescriptionSimpleInfoDTO> findQuizRightDescriptions(Long questionId){
//        List<Description> descriptions = descriptionRepository.findByQuestionId(questionId);
//        Question question = questionRepository.findById(questionId).orElseThrow(NotFoundByIdException::new);
//        for(Re)
//
//
//    }

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
