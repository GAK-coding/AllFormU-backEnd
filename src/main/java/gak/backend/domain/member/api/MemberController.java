package gak.backend.domain.member.api;

import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class MemberController {

    @Autowired
    MemberRepository memberRepository;
    @PostMapping("/api/signup")
    public String add(@RequestBody Member member){
        memberRepository.save(member);
        return "update OK";
    }
    @PostMapping("/api/login")
    @ResponseBody
    public List<Member> login(@RequestBody Member member){
        List<Member> user = memberRepository.findByEmail(member.getEmail());


        return user;
    }
    @GetMapping("/api/item")
    public List<Member> get(){
        return memberRepository.findAll();
    }

    @GetMapping("/api/item/{id}")
    public Optional<Member> getId(@PathVariable("id")Long id){
        return memberRepository.findById(id);
    }

}