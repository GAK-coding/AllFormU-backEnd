package gak.backend.global.error;

public class NotFoundByIdException extends IllegalArgumentException{
    public NotFoundByIdException(){super("해당아이디로 존재하는 객체를 찾을 수 없습니다.");}

    public NotFoundByIdException(String s){super(s);}
}
