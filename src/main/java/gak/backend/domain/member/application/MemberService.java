package gak.backend.domain.member.application;

import gak.backend.domain.file.application.FileService;
import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.exception.DormantMemberException;
import gak.backend.domain.member.exception.ExistMemberException;
import gak.backend.domain.member.exception.NotFoundMemberByEmailException;
import gak.backend.domain.member.exception.NotMatchPasswordException;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.global.error.ErrorResponse;
import gak.backend.global.error.exception.NotFoundByIdException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

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
    2. 휴면 계정을 할건지 완전 탈퇴할건지. 완전 탈퇴라면 우리가 안보이게 처리해야함. -> id로 구분할거임.

 */

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final FileService fileService;

    //TODD 버튼 따로 뺄거니까 컨트롤러, 서비스 새로 로직 생성.
    //상태가 멤버인 계정이 있는지 확인하는 서비스 로직
    @Transactional
    public ErrorResponse checkDuplicatedMember(EmailDTO emailDTO) {
        if (memberRepository.existsByEmail(emailDTO.getEmail())) {
            List<Member> members = memberRepository.findMembersByEmail(emailDTO.getEmail());
            for (Member member : members) {
                if (member.getStatus() == Status.STATUS_MEMBER) {
                    throw new ExistMemberException();
                } else if (member.getStatus() == Status.STATUS_DORMANT) {
                    throw new DormantMemberException();
                }
            }
        }
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("사용가능한 이메일입니다.")
                .build();
    }

    //멤버 생성이니까 회원가입.
    @Transactional
    public MemberInfoDTO createMember(MemberSaveRequest memberSaveRequest) {
        //int cnt = 0; //이메일로 이미 한번이라도 계정을 만든 사용자 중, 탈퇴 회원 수 새서 확인해보기 위함.
        //이미 존재하는 멤버이면, 스타터스를 보고 휴면 계정을 돌려주고 휴면 계정에서 회원가입할 경우, 그냥 바로 스타터스를 멤버로?, 처음 생성이라면
        if (memberRepository.existsByEmail(memberSaveRequest.getEmail())) {
            //이미 존재하는 이메일이라면, 멤버 목록을 보고 휴면 계정인것을 찾음. 리스트인 이유는 탈퇴, 탈퇴, 휴면했을 경우도 있을 수 있기 때문.
            List<Member> members = memberRepository.findMembersByEmail(memberSaveRequest.getEmail());
            for (Member member : members) {
                if (member.getStatus() == Status.STATUS_DORMANT) {
                    //휴면상태인 멤버가 회원가입을 요청하면, 멤버 상태로 바꿔줌.
                    member.UpdateMemberStatus(Status.STATUS_MEMBER);
                    MemberInfoDTO memberInfoDTO = member.toMemberInfoDTO();
                    return memberInfoDTO;
                }
//                else cnt++;
            }
//            //만약에 다 탈퇴했을 경우도 고려해주기
//            if (cnt == members.size()) {//멤버 목록도 있으나 다 탈퇴한 경우
//                Member reMember = memberSaveRequest.toEntity();
//                memberRepository.save(reMember);
//                MemberInfoDTO memberInfoDTO = reMember.toMemberInfoDTO();
//                return memberInfoDTO;
//            }

        }
        //존재하지 않는 이메일이라면, 신규 회원임.
        Member member = memberSaveRequest.toEntity();
        memberRepository.save(member);
        MemberInfoDTO memberInfoDTO = member.toMemberInfoDTO();
        //레포지토리에 저장된 것을 확인하기 위해서 레포지토리에서 멤버를 불러와서 바꾸어할지 아니면 그냥 해도 될지 사실 난 이게 맞다고 봄 시간이 좀 느려지긴해도
        return memberInfoDTO;


    }

    //로그인 할 때는 조회를 하는것이지만 상태를 확인해야함. 휴면, 탈퇴한 회원이 로그인을 시도한다면, 막아줘야함.
    //그래야 로그인 상태에서 조회를 할 때, 탈퇴한 회원, 휴면인 회원을 제외 시킬 수 있음.
    @Transactional
    public MemberInfoDTO loginMember(LoginReqeust loginReqeust) {
        int cnt = 0;
        //우선 해당 이메일로 존재하는 계정들이 있는지 찾음 있다면 다음과 같이 진행, 없다면, 예외
        if (memberRepository.existsByEmail(loginReqeust.getEmail())) {
            List<Member> members = memberRepository.findMembersByEmail(loginReqeust.getEmail());
            for (Member member : members) {
                if (member.getStatus() == Status.STATUS_MEMBER) {
                    //비밀번호가 다를 경우 예외 처리
                    if (!member.getPassword().equals(loginReqeust.getPassword())) {
                        throw new NotMatchPasswordException("비밀번호가 일치하지 않습니다.");
                    }
                    return member.toMemberInfoDTO();
                }
                //휴면인 계정이 있다면, 휴면 계정이니 회원가입해주세요.
                else if (member.getStatus() == Status.STATUS_DORMANT) {
                    throw new DormantMemberException("휴면 계정입니다. 회원가입 해주세요.");
                }
                else cnt++;
            }
            //이메일로 회원가입했던 흔적은 존재하나, 전부 탈퇴했던 회원은 아예 처음 온 회원이랑 같음.
            if (cnt == members.size()) {
                throw new NotFoundMemberByEmailException();
            }
        }
        throw new NotFoundMemberByEmailException();
    }


//================================================Read==========================================

    //멤버 전체 조회
    @Transactional(readOnly = true)
    public List<MemberInfoDTO> readTotalMemberInfoDTO(){
        List<Member> members = memberRepository.findAll();
        List<MemberInfoDTO> mList = new ArrayList<>();
        for(Member m : members){
            mList.add(m.toMemberInfoDTO());
        }
        return mList;
    }

    //아이디로 멤버 객체 조회
    @Transactional(readOnly = true)
    public Member readMemberById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return member;
    }

    //아이디로 멤버 infoDTO 조회
    @Transactional(readOnly = true)
    public MemberInfoDTO readMemberDTOById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        return member.toMemberInfoDTO();
    }


    //이메일로 멤버 조회
    @Transactional(readOnly = true)
    public Member readMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundByIdException::new);
        return member;
    }

    //이메일로 멤버 infoDTO 조회
    @Transactional(readOnly = true)
    public MemberInfoDTO readMemberDTOByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberByEmailException::new);
        return member.toMemberInfoDTO();
    }

    //이메일로 비밀번호 조회
    //TODO 랜덤값으로 수정
    //TODO 비밀 번호 모를시에, 랜덤값으로 넘겨주고 변경하게 하기
    @Transactional(readOnly = true)
    public String findPasswordByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(NotFoundMemberByEmailException::new);
        return member.getPassword();
    }


    //비밀번호 찾기 랜덤 숫자 메일로 발송

    //=========================================Update==================================
    //멤버 닉네임 업데이트
    @Transactional
    public UpdateNicknameDTO updateMemberNickname(UpdateNicknameRequest updateNicknameRequest) {
        Member member = memberRepository.findById(updateNicknameRequest.getId()).orElseThrow(NotFoundMemberByEmailException::new);
        member.UpdateMemberNickname(updateNicknameRequest.getNewNickname());
        log.info(memberRepository.findById(updateNicknameRequest.getId()).orElseThrow(NotFoundByIdException::new)+"gn");
        UpdateNicknameDTO updateNicknameDTO = member.toUpdateNicknameDTO();
        return updateNicknameDTO;
    }

    //멤버 비밀번호 변경, 사용자가 비밀번호도 입력으로 받고 실제 번호와 일치하는지 확인
    @Transactional
    //TODO 비밀번호 암호화하기
    public UpdatePasswordDTO updateMemberPasswordById(UpdatePasswordRequest updatePasswordRequest) {
        Member member = memberRepository.findById(updatePasswordRequest.getId()).orElseThrow(NotFoundByIdException::new);
        System.out.println(member.getPassword());
        System.out.println((updatePasswordRequest.getPassword()));
        if (!member.getPassword().equals(updatePasswordRequest.getPassword())) {
            throw new NotMatchPasswordException();
        } else {
            member.UpdateMemberPassword(updatePasswordRequest.getNewPwd());
        }
        return member.toUpdatePasswordDTO();
    }
    //===========================================Delete(사실 멤버는 삭제가 없음.) 업데이트임======================================

    //멤버 프로필사진 수정
    @Transactional
    public UpdateImageDTO updateMemberImage(UpdateImageRequest updateImageRequest) {

        Member member = memberRepository.findById(updateImageRequest.getId()).orElseThrow(NotFoundMemberByEmailException::new);

        // 기존 이미지 값이 "/images/userProfile.png"이 아닌 경우 삭제
        if (!member.getImage().equals("/images/userProfile.png")) {
            fileService.deleteImageByUrl(member.getImage());
        }

        member.UpdateMemberImage(updateImageRequest.getNewImage());
        log.info(memberRepository.findById(updateImageRequest.getId()).orElseThrow(NotFoundByIdException::new)+"gn");

        UpdateImageDTO updateImageDTO = member.toUpdateImageDTO();
        return updateImageDTO;
    }
    //"/images/userProfile.png"==기본이미지 춘식이면 바로 이미지 업데이트 / 춘식이가 아닌 사용자가 이전에 s3에 올린 이미지를 다른 이미지로 수정하는거면 기존 이미지 삭제
    //휴면 계정으로 전환
    @Transactional
    public MemberStatusInfoDTO changeMemberStatusDormant(Long id){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        member.UpdateMemberStatus(Status.STATUS_DORMANT);
        MemberStatusInfoDTO memberStatusInfoDTO = member.toMemberStatusInfoDTO();
        return memberStatusInfoDTO;
    }

    //회원 탈퇴
    @Transactional
    public MemberStatusInfoDTO changeMemberStatusWithdrawal(Long id){
        Member member = memberRepository.findById(id).orElseThrow(NotFoundByIdException::new);
        member.UpdateMemberStatus(Status.STATUS_WITHDRAWAL);
        MemberStatusInfoDTO memberStatusInfoDTO = member.toMemberStatusInfoDTO();
        return memberStatusInfoDTO;
    }
}
