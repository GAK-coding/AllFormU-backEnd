package gak.backend.domain.member.api;

import gak.backend.domain.member.application.MemberService;
import gak.backend.domain.member.application.RegisterMailService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static gak.backend.domain.member.dto.MemberDTO.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class MemberController {
    private final MemberService memberService;
    private final RegisterMailService registerMailService;
    //회원가입
    @PostMapping(value="/member/register")
    public ResponseEntity<MemberInfoDTO> createMember(@RequestBody @Validated MemberSaveRequest memberSaveRequest){
        MemberInfoDTO memberInfoDTO = memberService.createMember(memberSaveRequest);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.CREATED);
    }
    //사용가능한 이메일인지 확인
    @PostMapping(value="/member/check/duplicatedMember/{email}")
    public ResponseEntity<String> checkDuplicatedMember(@PathVariable(name="email") String email){
        String s = memberService.checkDuplicatedMember(email);
        return new ResponseEntity<>(s, HttpStatus.OK);
    }
    //이메일 인증 코드 보내기
    @PostMapping("/member/register/confirm/{email}")
    public ResponseEntity<String> mailConfirm(@PathVariable(name="email") String email) throws Exception {
        String code = registerMailService.sendSimpleMessage(email);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    //로그인
    @PostMapping(value="/member")
    public ResponseEntity<MemberInfoDTO> loginMember(@RequestBody @Validated LoginReqeust loginReqeust){
        MemberInfoDTO memberInfoDTO = memberService.loginMember(loginReqeust);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.OK);
    }

    //멤버 아이디로 멤버 조회_infoDTO임
    @GetMapping(value="/member/read/{member_id}")
    public ResponseEntity<MemberInfoDTO> readMember(@PathVariable(name="member_id") Long id){
        MemberInfoDTO memberInfoDTO = memberService.readMemberDTOById(id);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.OK);
    }

    //TODO 닉네임 수정 로직 검토
    @PatchMapping(value="/member/update/nickname")
    public ResponseEntity<UpdateNicknameDTO> updateNickname(@RequestBody @Validated UpdateNicknameRequest updateNicknameRequest){
        UpdateNicknameDTO updateNicknameDTO = memberService.updateMemberNickname(updateNicknameRequest);
        return new ResponseEntity<>(updateNicknameDTO, HttpStatus.OK);
    }

    //비밀번호 변경
    @PatchMapping(value="/member/update/password")
    public ResponseEntity<UpdatePasswordDTO> updatePassword(@RequestBody @Validated UpdatePasswordRequest updatePasswordRequest){
        UpdatePasswordDTO updatePasswordDTO = memberService.updateMemberPasswordById(updatePasswordRequest);
        return new ResponseEntity<>(updatePasswordDTO, HttpStatus.OK);
    }

    @PatchMapping(value="/member/dormant/{member_id}")
    public ResponseEntity<MemberStatusInfoDTO> dormantMember(@PathVariable(name="member_id")Long id){
        MemberStatusInfoDTO memberStatusInfoDTO = memberService.changeMemberStatusDormant(id);
        return new ResponseEntity<>(memberStatusInfoDTO, HttpStatus.OK);
    }

    @PatchMapping(value="/member/withdrawal/{member_id}")
    public ResponseEntity<MemberStatusInfoDTO> withdrawalMember(@PathVariable(name="member_id")Long id){
        MemberStatusInfoDTO memberStatusInfoDTO = memberService.changeMemberStatusWithdrawal(id);
        return new ResponseEntity<>(memberStatusInfoDTO, HttpStatus.OK);
    }



//    @Autowired
//    MemberRepository memberRepository;
//    @PostMapping("/api/signup")
//    public String add(@RequestBody Member member){
//        memberRepository.save(member);
//        return "update OK";
//    }
//    @PostMapping("/api/login")
//    @ResponseBody
//    public List<Member> login(@RequestBody Member member){
//        List<Member> user = memberRepository.findByEmail(member.getEmail());
//
//
//        return user;
//    }
//    @GetMapping("/api/item")
//    public List<Member> get(){
//        return memberRepository.findAll();
//    }
//
//    @GetMapping("/api/item/{id}")
//    public Optional<Member> getId(@PathVariable("id")Long id){
//        return memberRepository.findById(id);
//    }

}