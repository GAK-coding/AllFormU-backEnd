package gak.backend.form;

import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.question.model.Question;
import gak.backend.member.MemberServiceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class FormServiceTest {
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

    //테스트 시작하면 레포지토리에 있는 데이터를 다 지움  하나씩 시작할때마다
    @BeforeEach
    public void deleteAll() {
        memberRepository.deleteAll();
        formRepository.deleteAll();
        questionRepository.deleteAll();
    }

    public MemberDTO.MemberSaveRequest memberSaveRequest1() {
        return MemberDTO.MemberSaveRequest.builder()
                .nickname("설문생성자")
                .email("sangsung@naver.com")
                .password("12345678")
                .image(null)
                .build();
    }

    public FormDTO.AllFormData allFormData1() {
        List<QuestionDTO> questions = new ArrayList<>();
        List<String> timeout = new ArrayList<>();
        timeout.add("2023 06 06 10 00 00"); // 예시로 시간 값을 추가
        timeout.add("2023 06 07 11 00 11");
        // QuestionDTO 객체를 생성하여 questions 리스트에 추가해야 함
        QuestionDTO questionDTO = QuestionDTO.builder()
                .title("질문 제목")
                .required(true)
                .sectionNum(1)
                .build();
        questions.add(questionDTO);

        return FormDTO.AllFormData.builder()
                .title("설문 제목")
                .content("설문 설명")
                .questions(questions)
                .timeout(timeout)
                .build();
    }

    @Test
    @DisplayName("폼 생성 성공 테스트 - 새로운 폼")
    public void createFormSuccessTest() throws Exception {
        // given
        FormDTO.AllFormData allFormData = allFormData1();
        MemberDTO.MemberSaveRequest memberSaveRequest = memberSaveRequest1();

        // Member 저장
        MemberDTO.MemberInfoDTO savedMember = memberService.createMember(memberSaveRequest);

        // when
        Long formId = formService.createForm(allFormData, savedMember.getId());

        // then
        Assertions.assertThat(formId).isNotNull();
        Assertions.assertThat(formRepository.findById(formId)).isPresent();
    }



}
