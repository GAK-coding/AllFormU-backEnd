package gak.backend.description;

import gak.backend.domain.description.application.DescriptionService;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.description.dto.DescriptionDTO;
import gak.backend.domain.description.model.Description;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.model.Question;
import gak.backend.domain.selection.application.SelectionService;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.dto.SelectionDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DescriptionServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    FormRepository formRepository;
    @Autowired
    FormService formService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    QuestionService questionService;
    @Autowired
    DescriptionRepository descriptionRepository;
    @Autowired
    DescriptionService descriptionService;

    @BeforeEach
    public void deleteAll(){
        memberRepository.deleteAll();
        formRepository.deleteAll();
        questionRepository.deleteAll();
        descriptionRepository.deleteAll();
    }

    //그냥 하나씩 하기 귀찮으니까 박아놓음
//    public DescriptionDTO.DescriptionSaveRequest DescriptionSaveRequest1(){
//        return DescriptionDTO.DescriptionSaveRequest.builder()
//                .question_id(1L)
//                .content("주관식정답")
//                .build();
//    }
//    public DescriptionDTO.DescriptionSaveRequest descriptionSaverequest(Long id, String content){
//        return DescriptionDTO.DescriptionSaveRequest.builder()
//                .question_id(id)
//                .content(content)
//                .build();
//    }
//
//    @Test
//    @DisplayName("주관식 생성")
//    public void createDescriptionFailByDormantTest() throws Exception{
//        DescriptionDTO.DescriptionSaveRequest DescriptionSaveRequest1=DescriptionSaveRequest1();
//        Form form=new Form();
//        Member member=new Member();
//        //List<DescriptionSaveRequest> descriptionSaveRequests뭐로 받아와야하지
//        //descriptionService.createDescription(form.getId(),member.getId(),DescriptionSaveRequest1)
//
//    }
    @Test
    @DisplayName("주관식 생성")
    public void createDescription() {
        // given
        Member member = new Member();
        Question question = new Question();
        String content = "설명 내용";

        // when
        DescriptionDTO.DescriptionSaveRequest saveRequest = DescriptionDTO.DescriptionSaveRequest.builder()
                .question_id(question.getId())
                .content(content)
                .build();
        Description description = saveRequest.of(member, question);

        // then
        Assertions.assertThat(description.getMember()).isEqualTo(member);
        Assertions.assertThat(description.getQuestion()).isEqualTo(question);
        Assertions.assertThat(description.getContent()).isEqualTo(content);

    }



}
