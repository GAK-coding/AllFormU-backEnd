package gak.backend.domain.member.application;

import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.exception.NotFoundMemberByEmailException;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.global.error.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    public MemberInfoDTO createMember(MemberSaveRequest memberSaveRequest) {
        //이미 존재하는 멤버이면, 스타터스를 보고 휴면 계정을 돌려주고, 처음 생성이라면
        //이메일이 이미 존재하는 회원이라면,
        if (memberRepository.existsByEmail(memberSaveRequest.getEmail())) {
            //이미 존재하는 이메일이라면, 멤버 목록을 보고 휴면 계정인것을 찾음. 리스트인 이유는 탈퇴, 탈퇴, 휴면했을 경우도 있을 수 있기 때문.
            List<Member> members = memberRepository.findMembersByEmail(memberSaveRequest.getEmail());
            for (Member member : members) {
                if (member.getStatus() == Status.STATUS_DORMANT) {
                    MemberInfoDTO memberInfoDTO = member.toMemberInfoDTO();
                    return memberInfoDTO;
                }
                //위치 다시 고려해주기
                else{//멤버 목록도 있으나 다 탈퇴한 경우
                    Member reMember = memberSaveRequest.toEntity();
                    memberRepository.save(reMember);
                    MemberInfoDTO memberInfoDTO = reMember.toMemberInfoDTO();
                    return memberInfoDTO;
                }

                //만약에 다 탈퇴했을 경우도 고려해주기
            }

        } else {//존재하지 않는 이메일이라면, 신규 회원임.
            Member member = memberSaveRequest.toEntity();
            memberRepository.save(member);
            MemberInfoDTO memberInfoDTO = member.toMemberInfoDTO();
            //레포지토리에 저장된 것을 확인하기 위해서 레포지토리에서 멤버를 불러와서 바꾸어할지 아니면 그냥 해도 될지 사실 난 이게 맞다고 봄 시간이 좀 느려지긴해도
            return memberInfoDTO;
        }
        return null;
    }

//================================================Read==========================================

    //아이디로 멤버 객체 조회
    public Member readMemberById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return member;
    }
    //아이디로 멤버 infoDTO 조회
    public MemberInfoDTO readMemberDTOById(Long id){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return member.toMemberInfoDTO();
    }


    //이메일로 멤버 조회
    public Member readMemberByEmail(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundByIdException::new);
        return member;
    }
    //이메일로 멤버 infoDTO 조회
    public MemberInfoDTO readMemberDTOByEmail(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberByEmailException::new);
        return member.toMemberInfoDTO();
    }

    //이메일로 비밀번호 조회
    public int findPasswordByEmail(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberByEmailException::new);
        return member.getPassword();
    }




    //=========================================Update==================================




    //===========================================Delete======================================

}
