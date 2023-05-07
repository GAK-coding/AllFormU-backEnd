package gak.backend.domain.member.api;

import gak.backend.domain.member.application.MemberService;
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
    @PostMapping(value="/member/register")
    public ResponseEntity<MemberInfoDTO> createMember(@RequestBody @Validated MemberSaveRequest memberSaveRequest){
        MemberInfoDTO memberInfoDTO = memberService.createMember(memberSaveRequest);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.CREATED);
    }
    //TODO 로그인 수정 POST임
    @GetMapping(value="/member")
    public ResponseEntity<MemberInfoDTO> loginMember(@RequestBody @Validated LoginReqeust loginReqeust){
        MemberInfoDTO memberInfoDTO = memberService.loginMember(loginReqeust);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.OK);
    }

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

    @PatchMapping(value="/member/update/password")
    public ResponseEntity<UpdatePasswordDTO> updatePassword(@RequestBody @Validated UpdatePasswordRequest updatePasswordRequest){
        UpdatePasswordDTO updatePasswordDTO = memberService.updateMemberPasswordById(updatePasswordRequest);
        return new ResponseEntity<>(updatePasswordDTO, HttpStatus.OK);
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