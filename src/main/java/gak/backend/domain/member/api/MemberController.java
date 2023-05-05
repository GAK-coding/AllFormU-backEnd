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


    @Autowired
    MemberRepository memberRepository;

//    @PostMapping("/api/login")
//    @ResponseBody
//    public List<Member> login(@RequestBody Member member){
//        List<Member> user = memberRepository.findByEmail(member.getEmail());
//
//
//        return user;
//    }
    @GetMapping("/api/item")
    public List<Member> get(){
        return memberRepository.findAll();
    }
//
//    @GetMapping("/api/item/{id}")
//    public Optional<Member> getId(@PathVariable("id")Long id){
//        return memberRepository.findById(id);
//    }

}