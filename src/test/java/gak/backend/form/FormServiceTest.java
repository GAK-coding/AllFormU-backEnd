package gak.backend.form;

import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.dto.FormDTO;
import gak.backend.domain.form.exception.NotFoundMemberException;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.exception.ExistMemberException;
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
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
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
        List<Form> forms = formRepository.findAll();
        for (Form form : forms) {
            form.setAuthor(null);
        }
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
    @Transactional
    @DisplayName("폼 생성 성공 테스트 - 새로운 폼")
    public void createFormSuccessTest() {
        // given
        MemberDTO.MemberSaveRequest memberSaveRequest = MemberDTO.MemberSaveRequest.builder()
                .nickname("설문생성자")
                .email("sangsung@naver.com")
                .password("12345678")
                .image(null)
                .build();
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
        MemberDTO.MemberInfoDTO savedMember = memberService.createMember(memberSaveRequest);

        FormDTO.AllFormData formData = FormDTO.AllFormData.builder()
                .authorId(savedMember.getId()) // 수정: authorId 설정
                .questions(questions)
                .title("설문 제목")
                .content("설문 설명")
                .timeout(timeout)
                .build();

        // when
        assertDoesNotThrow(() -> formService.createForm(formData, savedMember.getId())); // 수정: 예외 발생 여부 확인

        // then
        List<Form> forms = formService.getFormById(savedMember.getId());
        Assertions.assertThat(forms).isNotEmpty();
        Assertions.assertThat(forms.size()).isEqualTo(1);
        Form form = forms.get(0);
        Assertions.assertThat(form.getAuthor().getId()).isEqualTo(savedMember.getId()); // 수정: 작성자 ID 일치 여부 확인
        Assertions.assertThat(form.getTitle()).isEqualTo(formData.getTitle());
    }


    @Test
    @DisplayName("폼 생성 실패 테스트 - 멤버가 없는 경우")
    public void createFormFailureTest() throws Exception {
        // given
        MemberDTO.MemberSaveRequest memberSaveRequest = MemberDTO.MemberSaveRequest.builder()
                .nickname("설문생성자")
                .email("sangsung@naver.com")
                .password("12345678")
                .image(null)
                .build();
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
        MemberDTO.MemberInfoDTO savedMember = memberService.createMember(memberSaveRequest);

        FormDTO.AllFormData formData = FormDTO.AllFormData.builder()
                .authorId(savedMember.getId()) // 수정: authorId 설정
                .questions(questions)
                .title("설문 제목")
                .content("설문 설명")
                .timeout(timeout)
                .build();

        // when & then
        assertThrows(NotFoundMemberException.class, () -> {
            formService.createForm(formData, 9999L); // 존재하지 않는 멤버 ID 전달
        });
    }


    @Test
    @DisplayName("폼 조회 테스트")
    public void getFormByIdTest() {
        // given
        MemberDTO.MemberSaveRequest memberSaveRequest = memberSaveRequest1();

        // Member 저장
        MemberDTO.MemberInfoDTO savedMember = memberService.createMember(memberSaveRequest);

        // 폼 생성
        FormDTO.AllFormData allFormData = allFormData1();
        Long formId = formService.createForm(allFormData, savedMember.getId());

        // when
        List<Form> forms = formService.getFormById(savedMember.getId());

        // then
        Assertions.assertThat(forms).isNotEmpty();
        Assertions.assertThat(forms.size()).isEqualTo(1);
        Form form = forms.get(0);
        Assertions.assertThat(form.getId()).isEqualTo(formId);
        Assertions.assertThat(form.getTitle()).isEqualTo(allFormData.getTitle());

    }



}
