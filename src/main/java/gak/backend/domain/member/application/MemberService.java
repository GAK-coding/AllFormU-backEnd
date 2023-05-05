package gak.backend.domain.member.application;

import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.exception.NotFoundMemberByEmailException;
import gak.backend.domain.member.exception.NotMatchPasswordException;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.global.error.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static gak.backend.domain.member.dto.MemberDTO.*;
/*
C(생성):
    1. 이메일이 중복인지 확인해야함.
    2. 휴면 계정 여부 확인해야함.
    3.
R(조회):
    1. required form list 확인
    2. response form list 확인
    3.
U(수정):
    1. 자신이 작성한 것만 수정할 수 있고
    2. 이미 응답자가 있다면 수정하지 못함.
    3. 응답자가 없다면 수정할 수 있음.
D(삭제):
    1. 본인인지 확인.
    2. 휴면 계정을 할건지 완전 탈퇴할건지. 완전 탈퇴라면 우리가 안보이게 처리해야함.

 */

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //멤버 생성이니까 회원가입.
    @Transactional
    public MemberInfoDTO createMember(MemberSaveRequest memberSaveRequest) {
        int cnt = 0; //이메일로 이미 한번이라도 계정을 만든 사용자 중, 탈퇴 회원 수 새서 확인해보기 위함.
        //이미 존재하는 멤버이면, 스타터스를 보고 휴면 계정을 돌려주고, 처음 생성이라면
        //이메일이 이미 존재하는 회원이라면,
        //TODO 회원가입 하려고 하는데 이미 멤버로 존재할 경우 로직 짜기
        if (memberRepository.existsByEmail(memberSaveRequest.getEmail())) {
            //이미 존재하는 이메일이라면, 멤버 목록을 보고 휴면 계정인것을 찾음. 리스트인 이유는 탈퇴, 탈퇴, 휴면했을 경우도 있을 수 있기 때문.
            List<Member> members = memberRepository.findMembersByEmail(memberSaveRequest.getEmail());
            for (Member member : members) {
                if (member.getStatus() == Status.STATUS_DORMANT) {
                    MemberInfoDTO memberInfoDTO = member.toMemberInfoDTO();
                    return memberInfoDTO;
                }
                else cnt++;
            }
            //만약에 다 탈퇴했을 경우도 고려해주기
            //TODO 위치 다시 고려해주기
            if (cnt == members.size()) {//멤버 목록도 있으나 다 탈퇴한 경우
                Member reMember = memberSaveRequest.toEntity();
                memberRepository.save(reMember);
                MemberInfoDTO memberInfoDTO = reMember.toMemberInfoDTO();
                return memberInfoDTO;
            }

        }
        //존재하지 않는 이메일이라면, 신규 회원임.
        Member member = memberSaveRequest.toEntity();
        memberRepository.save(member);
        MemberInfoDTO memberInfoDTO = member.toMemberInfoDTO();
        //레포지토리에 저장된 것을 확인하기 위해서 레포지토리에서 멤버를 불러와서 바꾸어할지 아니면 그냥 해도 될지 사실 난 이게 맞다고 봄 시간이 좀 느려지긴해도
        return memberInfoDTO;


    }


//================================================Read==========================================

    //아이디로 멤버 객체 조회
    @Transactional
    public Member readMemberById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return member;
    }
    //아이디로 멤버 infoDTO 조회
    @Transactional
    public MemberInfoDTO readMemberDTOById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return member.toMemberInfoDTO();
    }


    //이메일로 멤버 조회
    @Transactional
    public Member readMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundByIdException::new);
        return member;
    }
    //이메일로 멤버 infoDTO 조회
    @Transactional
    public MemberInfoDTO readMemberDTOByEmail(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberByEmailException::new);
        return member.toMemberInfoDTO();
    }

    //이메일로 비밀번호 조회
    @Transactional
    public String findPasswordByEmail(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberByEmailException::new);
        return member.getPassword();
    }




    //=========================================Update==================================
    //TODO update return값 DTO로 수정해주기
    //멤버 닉네임 업데이트
    @Transactional
    public String updateMemberNickname(Long id, String newNickname){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundMemberByEmailException::new);
        member.updateMemberNickname(newNickname);
        return "UPDATE";
    }

    //멤버 비밀번호 변경, 사용자가 비밀번호도 입력으로 받고 실제 번호와 일치하는지 확인
    //TODO 비밀 번호 모를시에, 랜덤값으로 넘겨주고 변경하게 하기
    @Transactional
    public String updateMemberPasswordById(Long id, String password, String newPwd){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        if(member.getPassword()!= password){
            throw new NotMatchPasswordException();
        }
        else{
            member.updateMemberPassword(newPwd);
        }
        return "Password Update";
    }



    //===========================================Delete======================================
    @Transactional
    public String deleteMember(Long id){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        //사용자에게 휴면 계정으로 할것인지 여부를 물어보는 것을 어떻게 지정해줘야하지? 프론트와 상의해봐야할 듯.
        //delete status 컬럼을 추가해줘야할 것 같기도 하다.

        return "DELETE";
    }
}
