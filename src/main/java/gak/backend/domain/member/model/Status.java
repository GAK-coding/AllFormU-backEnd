package gak.backend.domain.member.model;

public enum Status {
    //각각 회원, 탈퇴한 회원, 휴면 회원(재회원가입시 복구 가능)
    STATUS_MEMBER, STATUS_WITHDRAWAL, STATUS_DORMANT
}
