package gak.backend.domain.member.application;

import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.dto.MemberDTO.MemberSaveRequest;
import gak.backend.domain.member.dto.MemberDTO.OperatorDTO;
import gak.backend.domain.member.exception.DormantMemberException;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService{
    private final MemberRepository memberRepository;
    private Map<String, Object> attributes;

//    //UserDetails : Form 로그인 시 사용
//    public MemberDetailService()

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        List<Member> members = memberRepository.findMembersByEmail(email);
        Member member=null;
        for(Member m : members){
            if(m.getStatus()== Status.STATUS_MEMBER) {
                member = m;
                break;
            }
            else if(m.getStatus()==Status.STATUS_DORMANT){
                throw new DormantMemberException("휴면 계정입니다. 회원가입 해주세요.");
            }
        }
        if(member==null){
            throw new UsernameNotFoundException("존재하지 않는 사용자입니다.");
        }
        return createUser(member);
    }

    //Security user 정보 생성. db에 존재하면 UserDetails 객체로 만들어서 반환
    private UserDetails createUser(Member member){
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole().toString());

        return new User(
                String.valueOf(member.getId()),
                member.getPassword(),
                Collections.singleton(grantedAuthority)
        );


    }
}
