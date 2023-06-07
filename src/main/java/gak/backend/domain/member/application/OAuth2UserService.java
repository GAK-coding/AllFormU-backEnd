//package gak.backend.domain.member.application;
//
//import com.nimbusds.oauth2.sdk.GeneralException;
//import gak.backend.domain.member.dao.MemberRepository;
//import gak.backend.domain.member.model.*;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.UUID;
//
//@Service
//public class OAuth2UserService extends DefaultOAuth2UserService {
//    private MemberRepository memberRepository;
//    private PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
//
//        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId(); //구글
//        String providerId = oAuth2User.getAttribute("sub");
//        String username = provider + "_" + providerId; //사용자가 이름을 만든적이 없으나 만들어줌.
//
//        String uuid = UUID.randomUUID().toString().substring(0, 8);
//        String password = passwordEncoder.encode("패스워드" + uuid); //사용자가 비번을 입력한적이 없지만 만들어줌.
//
//        String email = oAuth2User.getAttribute("email");
//        //Role role = Role.Role_Responsor;
//
//        Member member = null;
//
//        List<Member> members = memberRepository.findMembersByEmail(email);
//        for (Member m : members) {
//            if (m.getStatus() == Status.STATUS_MEMBER) {
//                member = m;
//                break;
//            }
//            //휴면 계정이라면 멤버를 활성화 상태로 바꿔주어야함.
//            else if (m.getStatus() == Status.STATUS_DORMANT) {
//                member = m;
//                member.UpdateMemberStatus(Status.STATUS_MEMBER);
//            }
//        }
//        //DB에 없는 사용자면, 회원가입을 한다. 구글 연동은 바로 회원가입 처리해야함.
//        if (member == null) {
//            passwordEncoder.encode(password);
//            member = Member.builder()
//                    .nickname(username)
//                    .password(password)
//                    .email(email)
//                    .status(Status.STATUS_MEMBER)
//                    //.role(Role.Role_Responsor)
//                    .image(null)
//                    .build();
//            memberRepository.save(member);
//        }
//        return new UserPrinciple(member, oAuth2User.getAttributes());
//    }
//}
