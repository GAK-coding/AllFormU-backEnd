//package gak.backend.domain.member.application;
//
//import gak.backend.domain.member.dao.MemberRepository;
//import gak.backend.domain.member.dto.MemberDTO;
//import gak.backend.domain.member.dto.MemberDTO.MemberSaveRequest;
//import gak.backend.domain.member.dto.MemberDTO.OperatorDTO;
//import gak.backend.domain.member.model.Member;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@Slf4j
//@RequiredArgsConstructor
//public class MemberDetailService implements UserDetailsService {
//    private final MemberRepository memberRepository;
//
//    @Override
//    @Transactional
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
//        return memberRepository.findByEmail(email)
//                .map(member -> createUser(email, member))
//                .orElseThrow() -> new UsernameNotFoundException(email + "-> 존재하지 않음."));
//    }
//
//    //Security user 정보 생성
//    private User createUser(String email, OperatorDTO operatorDTO){
//        System.out.println(operatorDTO.getIsUse());
//        if(!"Y".equals(operatorDTO.getIsUse())){
//            throw new BadCredentialsException(email + "-> 활성화 되어있지 않습니다.");
//        }
//        List<GrantedAuthority> grantedAuthorities = operatorDTO.getAuthorities().stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.getNickname()))
//                .collect(Collectors.toList());
//        return new org.springframework.security.core.userdetails.User(
//                operatorDTO.getEmail(),
//                operatorDTO.getPassword(),
//                grantedAuthorities);
//
//    }
//}
