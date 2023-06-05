package gak.backend.question;

import gak.backend.domain.description.application.DescriptionService;
import gak.backend.domain.description.dao.DescriptionRepository;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.question.dto.QuestionDTO;
import gak.backend.domain.selection.application.SelectionService;
import gak.backend.domain.selection.dao.SelectionRepository;
import gak.backend.domain.selection.dto.SelectionDTO;
import org.junit.jupiter.api.BeforeEach;
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

//    public QuestionDTO QuestionSaveRequest1(){
//        return QuestionDTO.builder()
//
//    }
}
