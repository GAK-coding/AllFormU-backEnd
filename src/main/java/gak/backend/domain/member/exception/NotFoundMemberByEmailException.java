package gak.backend.domain.member.exception;

public class NotFoundMemberByEmailException extends IllegalArgumentException{
    public NotFoundMemberByEmailException(String m){ super(m);}
    public NotFoundMemberByEmailException(){super("해당 이메일을 사용하는 user가 존재하지 않습니다.");}
}
