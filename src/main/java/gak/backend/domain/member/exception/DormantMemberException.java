package gak.backend.domain.member.exception;

public class DormantMemberException extends IllegalArgumentException{
    public DormantMemberException(String m){super(m);}
    public DormantMemberException(){super("휴면 계정입니다.");}
}
