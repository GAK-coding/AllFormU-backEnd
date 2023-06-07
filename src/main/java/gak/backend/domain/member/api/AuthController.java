//package gak.backend.domain.member.api;
//
//import gak.backend.domain.member.dto.MemberDTO;
//import gak.backend.domain.member.dto.TokenDTO;
//import gak.backend.global.jwt.TokenProvider;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@Validated
//public class AuthController {
//
//    private final TokenProvider tokenProvider;
//    private final AuthenticationManagerBuilder authenticationManagerBuilder;
//
//    @PostMapping("/authenticate")
//    public ResponseEntity<TokenDTO> authorize(@RequestBody @Validated MemberDTO.OperatorDTO operatorDTO)
//
//}
