package gak.backend.response;

import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.response.dto.ResponseDTO.ResponseSimpleInfoDTO;
import gak.backend.domain.response.dto.ResponseDTO.ResponseListInfoDTO;
import gak.backend.domain.response.dto.ResponseDTO.ResponseInfoDTO;
import gak.backend.domain.question.application.QuestionService;
import gak.backend.domain.question.dao.QuestionRepository;
import gak.backend.domain.response.application.ResponseService;
import gak.backend.domain.response.dao.ResponseRepository;
import gak.backend.domain.response.dto.ResponseDTO;
import gak.backend.domain.selection.application.SelectionService;
import gak.backend.domain.selection.dao.SelectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ResponseServiceTest {
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
    SelectionRepository selectionRepository;
    @Autowired
    SelectionService selectionService;
    @Autowired
    ResponseRepository responseRepository;
    @Autowired
    ResponseService responseService;
    //테스트 시작하면 레포지토리에 있는 데이터를 다 지움  하나씩 시작할때마다
    @BeforeEach
    public void deleteAll(){
        memberRepository.deleteAll();
        formRepository.deleteAll();
        questionRepository.deleteAll();
        selectionRepository.deleteAll();
        responseRepository.deleteAll();
    }

    public ResponseSimpleInfoDTO ResponseSaveRequest1(){
        return ResponseSimpleInfoDTO.builder()
                .id(1L)
                .responsor_id(1L)
                .question_id(1L)
                .num(1)
                .build();
    }

//    public ResponseListInfoDTO ResponseSaveRequest2(){
//        return ResponseListInfoDTO.builder()
//                .responseList()
//    }

    public ResponseInfoDTO ResponseSaveRequest3(){
        return ResponseInfoDTO.builder()
                .id(1L)
                .responsor_id(1L)
                .question_id(1L)
                .num(1)
                //.createdTime()
                //.modifiedTime()
                .build();
    }



}
