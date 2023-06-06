package gak.backend.question;

import gak.backend.domain.description.application.DescriptionService;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.form.model.Form;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.question.model.Format;
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
public class QuestionServiceTest {
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
    @Autowired
    SelectionRepository selectionRepository;
    @Autowired
    SelectionService selectionService;

    @BeforeEach
    public void deleteAll(){
        memberRepository.deleteAll();
        formRepository.deleteAll();
        questionRepository.deleteAll();
        descriptionRepository.deleteAll();
        selectionRepository.deleteAll();
    }

//    public QuestionDTO.QuestionDTO QuestionSaveRequest1(){
//        return QuestionDTO.QuestionSaveRequest.builder()
//                .content("질문 내용")
//                .build();
//    }
    @Test
    @DisplayName("Question 생성 테스트")
    public void createQuestion() {
        // given
        Form form = new Form();
        String title = "질문 제목";
        String content = "질문 내용";
        Boolean required = true;
        Boolean quiz = false;
        Integer sectionNum = 1;
        Format type = Format.Description_SHORT;

        // when
        QuestionDTO questionDTO = QuestionDTO.builder()
                .form(form)
                .title(title)
                .content(content)
                .required(required)
                .quiz(quiz)
                .sectionNum(sectionNum)
                .type(type)
                .build();
        Question question = questionDTO.of(form);

        // then
        Assertions.assertThat(question).isNotNull();
        Assertions.assertThat(question.getForm()).isEqualTo(form);
        Assertions.assertThat(question.getTitle()).isEqualTo(title);
        Assertions.assertThat(question.getContent()).isEqualTo(content);
        //Assertions.assertThat(question.getRequired()).isEqualTo(required);
        //Assertions.assertThat(question.getQuiz()).isEqualTo(quiz);
        Assertions.assertThat(question.getSectionNum()).isEqualTo(sectionNum);
        Assertions.assertThat(question.getType()).isEqualTo(type);


    }
    @Test
    @DisplayName("Question 수정 테스트")
    public void updateQuestionTest() {
        // given
        Form form = new Form();
        Question question = new Question();
        String newTitle = "새로운 질문 제목";
        String newContent = "새로운 질문 내용";
//        Boolean newRequired = false;
//        Boolean newQuiz = true;
//        Integer newSectionNum = 2;
        Format newType = Format.Description_LONG;

        // when
        question.setTitle(newTitle);
        question.setContent(newContent);
        question.setForm(form);
        question.setType(newType);

        // then
        Assertions.assertThat(question.getTitle()).isEqualTo(newTitle);
        Assertions.assertThat(question.getContent()).isEqualTo(newContent);
        Assertions.assertThat(question.getForm()).isEqualTo(form);
        //Assertions.assertThat(question.getRequired()).isEqualTo(newRequired);
        //Assertions.assertThat(question.getQuiz()).isEqualTo(newQuiz);
        //Assertions.assertThat(question.getSectionNum()).isEqualTo(newSectionNum);
        Assertions.assertThat(question.getType()).isEqualTo(newType);
    }

    @Test
    @DisplayName("Question 삭제 테스트")
    public void deleteQuestionTest() {
        // given
        Form form = new Form();
        Question question = new Question();

        // when
        form.getQuestions().remove(question);

        // then
        Assertions.assertThat(form.getQuestions()).doesNotContain(question);
    }



}
