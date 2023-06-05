package gak.backend.member;

import com.amazonaws.services.ec2.model.Image;
import gak.backend.domain.form.application.FormService;
import gak.backend.domain.form.dao.FormRepository;
import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.dto.MemberDTO.EmailDTO;
import gak.backend.domain.member.dto.MemberDTO.MemberInfoDTO;
import gak.backend.domain.member.dto.MemberDTO.MemberSaveRequest;
import gak.backend.domain.member.exception.DormantMemberException;
import gak.backend.domain.member.exception.ExistMemberException;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.form.FormServiceTest;
import gak.backend.global.error.ErrorResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MemberServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    //테스트 시작하면 레포지토리에 있는 데이터를 다 지움  하나씩 시작할때마다
    @BeforeEach
    public void deleteAll(){
        memberRepository.deleteAll();
    }

    //그냥 하나씩 하기 귀찮으니까 박아놓음
    public MemberSaveRequest memberSaveRequest1(){
        return MemberSaveRequest.builder()
                .nickname("yunjjung")
                .email("yunjjung@naver.com")
                .password("12345678")
                .image(null)
                .build();
    }
    public MemberSaveRequest memberSaveRequest2(){
        return MemberSaveRequest.builder()
                .nickname("jjung")
                .email("jjung@naver.com")
                .password("12345678")
                .image(null)
                .build();
    }
    public MemberSaveRequest memberSaveRequest3(){
        return MemberSaveRequest.builder()
                .nickname("mung")
                .email("mung@naver.com")
                .password("12345678")
                .image(null)
                .build();
    }
    public MemberSaveRequest memberSaverequest(String nickname, String email, String pwd, String img){
        return MemberSaveRequest.builder()
                .nickname(nickname)
                .email(email)
                .password(pwd)
                .image(img)
                .build();
    }

    public EmailDTO duplicatedMemberCheckRequest(String email){
        return EmailDTO.builder()
                .email(email)
                .build();
    }

    //회원가입 테스트
    @Test
    @DisplayName("회원가입 성공 테스트_새로운 멤버")
    public void registerMemberSuccess() throws Exception{
        //given
        MemberSaveRequest saveRequest = memberSaveRequest1();
        EmailDTO emailDTO = duplicatedMemberCheckRequest(saveRequest.getEmail());

        //when
        memberService.checkDuplicatedMember(emailDTO);
        memberService.createMember(saveRequest);

        //then
        Assertions.assertThat( memberRepository.existsByEmail(saveRequest.getEmail())).isEqualTo(true);
    }

    @Test
    @Transactional//transactional떼야 하는데 우선 붙여둠.
    @DisplayName("회원가입 실패 테스트 : 이미 휴면 계정")
    public void registerMemberFailByDormantTest() throws Exception{
        //given
        //멤버 생성 요청
        MemberSaveRequest saveRequest = memberSaveRequest1();
        EmailDTO emailDTO = duplicatedMemberCheckRequest(saveRequest.getEmail());
        //멤버 저장
        MemberInfoDTO memberInfoDTO = memberService.createMember(saveRequest);
        Member member = memberRepository.findById(memberInfoDTO.getId()).get();
        member.UpdateMemberStatus(Status.STATUS_DORMANT);


        //when
        //똑같은 이메일로 요청 생성
        MemberSaveRequest saveRequest2 = memberSaveRequest1();
        EmailDTO emailDTO2 = duplicatedMemberCheckRequest(saveRequest2.getEmail());

        //then
        //Assertions.assertThat(memberService.checkDuplicatedMember(saveRequest2.getEmail())).isEqualTo(ErrorResponse<DormantMemberEx
        Assertions.assertThat(memberRepository.existsByEmail(member.getEmail())).isEqualTo(true);
        Assertions.assertThat(member.getStatus()).isEqualTo(Status.STATUS_DORMANT);
        assertThrows(DormantMemberException.class, () -> {
            ErrorResponse e = memberService.checkDuplicatedMember(emailDTO2);
        });

    }

    //회원가입 실패 테스트 _ 이미 존재하는 멤버
    @Test
    @DisplayName("회원가입 실패 테스트 : 이미 존재하는 멤버")
    public void registerMemberFailTest() throws Exception{
        //given
        //멤버 생성 요청
        MemberSaveRequest saveRequest = memberSaveRequest1();
        EmailDTO emailDTO = duplicatedMemberCheckRequest(saveRequest.getEmail());
        //멤버 저장
        MemberInfoDTO memberInfoDTO = memberService.createMember(saveRequest);
        Member member = memberRepository.findById(memberInfoDTO.getId()).get();


        //when
        //똑같은 이메일로 요청 생성
        MemberSaveRequest saveRequest2 = memberSaveRequest1();
        EmailDTO emailDTO2 = duplicatedMemberCheckRequest(saveRequest2.getEmail());

        //then
        Assertions.assertThat(memberRepository.existsByEmail(member.getEmail())).isEqualTo(true);
        assertThrows(ExistMemberException.class, () -> {
            ErrorResponse e = memberService.checkDuplicatedMember(emailDTO2);
        });

    }

    @Test
    @DisplayName("회원 상태 변경 테스트")
    public void changeMemberStatusTest() throws Exception{
        //given
        //멤버 생성 요청
        MemberSaveRequest saveRequest = memberSaveRequest1();
        EmailDTO emailDTO = duplicatedMemberCheckRequest(saveRequest.getEmail());
        //멤버 저장
        MemberInfoDTO memberInfoDTO = memberService.createMember(saveRequest);
        Member member = memberRepository.findById(memberInfoDTO.getId()).get();


        //when
        member.UpdateMemberStatus(Status.STATUS_DORMANT);

        //then
        Assertions.assertThat(memberRepository.existsByEmail(member.getEmail())).isEqualTo(true);
        Assertions.assertThat(member.getStatus()).isEqualTo(Status.STATUS_DORMANT);
//        assertThrows(DormantMemberException.class, () -> {
//            ErrorResponse e = memberService.checkDuplicatedMember(emailDTO2);
//        });

    }

}
