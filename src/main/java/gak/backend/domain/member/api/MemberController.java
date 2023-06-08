package gak.backend.domain.member.api;

import gak.backend.domain.member.application.MemberService;
//import gak.backend.domain.member.application.RegisterMailService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dao.RefreshTokenDao;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.dto.TokenDTO;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.UserPrincipal;
import gak.backend.global.error.ErrorResponse;
import gak.backend.global.error.exception.NotFoundByIdException;
import gak.backend.global.jwt.TokenProvider;
import gak.backend.global.service.MailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.hibernate5.SpringSessionContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import gak.backend.global.error.ErrorResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static gak.backend.domain.member.dto.MemberDTO.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class MemberController {
    //TODO !!!!!!!getMemberId코드 만들어서 리팩토링하기!!!!!!!111

    private final MemberService memberService;
    //private final RegisterMailService registerMailService;
    private final MailService mailService;
    //private final BCryptPasswordEncoder bCryptPasswordEncoder;
    //회원가입
    @PostMapping(value="/member/register")
    public ResponseEntity<MemberInfoDTO> createMember(@RequestBody @Validated MemberSaveRequest memberSaveRequest){
        MemberInfoDTO memberInfoDTO = memberService.createMember(memberSaveRequest);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.CREATED);
    }
    //사용가능한 이메일인지 확인
    @PostMapping(value="/member/check/duplicatedMember")
    public ResponseEntity<ErrorResponse> checkDuplicatedMember(@RequestBody @Validated EmailDTO emailDTO){
        ErrorResponse e = memberService.checkDuplicatedMember(emailDTO);
        return new ResponseEntity<>(e,HttpStatus.OK);
    }
    //회원가입 비밀번호 재설정 메일 보내기
    @PostMapping("/member/register/confirm")
    public ResponseEntity<ErrorResponse> mailConfirm(@RequestBody @Validated EmailReqest emailReqest) throws Exception {
        ErrorResponse code = mailService.sendSimpleMessage(emailReqest);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    //로그인==============================================
//    @PostMapping(value="/member")
//    public ResponseEntity<MemberInfoDTO> loginMember(@RequestBody @Validated LoginReqeust loginReqeust){
//        MemberInfoDTO memberInfoDTO = memberService.loginMember(loginReqeust);
//        return new ResponseEntity<>(memberInfoDTO, HttpStatus.OK);
//    }
    @PostMapping(value="/member/login")
    public ResponseEntity<MemberInfoTokenDTO> loginMember(@RequestBody @Validated LoginReqeust loginReqeust, HttpServletResponse response, HttpServletRequest request){
       //로그인 정보로 AuthenticationToken 생성
        MemberDTO.MemberInfoTokenDTO memberInfoTokenDTO = memberService.loginMember(loginReqeust, response, request);
        return new ResponseEntity<>(memberInfoTokenDTO, HttpStatus.OK);
//        MemberInfoDTO memberInfoDTO = memberService.loginMember(loginReqeust);
//        return new ResponseEntity<>(memberInfoDTO, HttpStatus.OK);
    }
    //구글 로그인
//    @PostMapping(value="/member/oauth")
//    public ResponseEntity<MemberInfoDTO> loginOAuth(Authentication authentication, @AuthenticationPrincipal UserPrinciple userPrinciple){
//        UserPrincipal principle = (UserPrincipal) authentication.getPrincipal();
//        if(principle.getMember().getProvider()==null){
//            log.info("Local login" + principle);
//        }
//        else{
//            log.info("OAuth2 login" + principle);
//        }
//        Member member = principle.getMember();
//        return new ResponseEntity<>(member.toMemberInfoDTO(), HttpStatus.OK);
//    }
    //멤버 전체조회
    @GetMapping(value = "/member/readTotalMember")
    public ResponseEntity<List<MemberInfoDTO>> readTotalMember(){
        List<MemberInfoDTO> mList = memberService.readTotalMemberInfoDTO();
        return new ResponseEntity<>(mList, HttpStatus.OK);
    }

    //member email로 자신이 등록했던 계정들 확인
    //공개되면 안되니까 바디에 담아서 감
    @GetMapping(value = "member/readMemberByEmail")
    public ResponseEntity<List<Member>> findMembersByEmail(@RequestBody @Validated EmailDTO emailDTO){
        List<Member> members = memberService.readMembersByEmail(emailDTO);
        return new ResponseEntity<>(members, HttpStatus.OK);
    }


    //멤버 아이디로 멤버 조회_infoDTO임
    @GetMapping(value="/member/read/{member_id}")
    public ResponseEntity<MemberInfoDTO> readMember(@PathVariable(name="member_id") Long id){
        MemberInfoDTO memberInfoDTO = memberService.readMemberDTOById(id);
        return new ResponseEntity<>(memberInfoDTO, HttpStatus.OK);
    }

    @PatchMapping(value="/member/update/nickname")
    public ResponseEntity<UpdateNicknameDTO> updateNickname(@RequestBody @Validated UpdateNicknameRequest updateNicknameRequest){
        UpdateNicknameDTO updateNicknameDTO = memberService.updateMemberNickname(updateNicknameRequest);
        return new ResponseEntity<>(updateNicknameDTO, HttpStatus.OK);
    }

    //프로필이미지변경
    @PatchMapping(value="/member/update/image")
    public ResponseEntity<UpdateImageDTO> updateImage(@RequestBody @Validated UpdateImageRequest updateImageRequest){
        UpdateImageDTO updateImageDTO = memberService.updateMemberImage(updateImageRequest);
        return new ResponseEntity<>(updateImageDTO, HttpStatus.OK);
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
    @GetMapping("/item")
    public List<Member> get(){
        return memberRepository.findAll();
    }
//
//    @GetMapping("/api/item/{id}")
//    public Optional<Member> getId(@PathVariable("id")Long id){
//        return memberRepository.findById(id);
//    }

    //============로그아웃=============

    @PostMapping(value = "/member/logout")
    public ResponseEntity<String> logout(HttpServletResponse response, HttpServletRequest request) throws IOException {
        //로그아웃 시에는 null 확인할 필요 없을 듯.
        memberService.logout(response, request);
        return new ResponseEntity<>("로그아웃되었습니다.", HttpStatus.OK);
    }

    //===========토큰 재발급=======
    @PostMapping(value="/member/auth/reissue")
    public ResponseEntity<TokenDTO> reissue(HttpServletRequest request, HttpServletResponse response){
        TokenDTO tokenDTO = memberService.reissue(request, response);
        return new ResponseEntity<>(tokenDTO, HttpStatus.OK);
    }


}