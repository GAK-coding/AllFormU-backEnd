package gak.backend.domain.member.exception;

public class ExistMemberException extends IllegalArgumentException{
    public ExistMemberException(String m){super(m);}
    public ExistMemberException(){super("이미 존재하는 회원입니다.");}
}
