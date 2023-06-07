package gak.backend.global.service;

import gak.backend.domain.member.dao.MemberRepository;
import gak.backend.domain.member.dto.MemberDTO;
import gak.backend.domain.member.dto.MemberDTO.EmailReqest;
import gak.backend.domain.member.exception.NotFoundMemberByEmailException;
import gak.backend.domain.member.model.Member;
import gak.backend.domain.member.model.Status;
import gak.backend.global.GlobalMethod;
import gak.backend.global.error.ErrorResponse;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    private final MemberRepository memberRepository;
    private final GlobalMethod globalMethod = new GlobalMethod();

    //인증번호 생성
    private String authNum = "";
    private String id;

    @Transactional
    public MimeMessage createMessage(String to, int n, String title, String type) throws MessagingException, UnsupportedEncodingException, jakarta.mail.MessagingException {
        //인증번호 생성
        authNum = globalMethod.makeRandom(n);
        log.info("보내는 대상" + to);
        log.info("인증 번호" + authNum);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); //보내는 대상
        message.setSubject(title); //제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1>안녕하세요</h1>";
        msg += "<h3>너를 위해서, 너만을 위해서<h3>";
        msg += "<h1>당신을 위한 설문 플랫폼 All Form U입니다.";
        msg += "<br>";
        msg += "<p>만족스러운 서비스를 제공하도록 노력하겠습니다. 감사합니다!";
        msg += "<div align='center' style='border:1px solid black; font-family:verdana; font-size:80%';>";
        msg += "<h3 style='color:blue'>";
        msg += type + "</h3>";
        msg += "<div>";
        msg += "CODE : <strong>";
        msg += authNum + "</strong><div><br/>"; //메일에 인증번호 넣기
        msg += "</div>";
        message.setText(msg, "utf-8","html");
        message.setFrom(new InternetAddress("allformu@naver.com", "All_Form_U")); //보내는 사람

        return message;
    }

    //인증코드 전송
//    public String createKey(){
//        StringBuffer key = new StringBuffer();
//        Random rand = new Random();
//
//        for(int i = 0; i < 8; i++) {
//            int index = rand.nextInt(3); // 0~2까지 랜덤, rand 값에 따라서 아래 switch문이 실행.
//
//            switch (index) {
//                case 0:
//                    key.append((char) ((int) (rand.nextInt(26)) + 97));
//                    //a-z (ex. 1+97 => 'b')
//                    break;
//                case 1:
//                    key.append((char) ((int) (rand.nextInt(26)) + 65));
//                    //A-Z
//                    break;
//                case 2:
//                    key.append((rand.nextInt(10)));
//                    //0-9
//                    break;
//            }
//        }
//        return key.toString();
//    }


    //메일 발송
    //senSimpleMessage의 매개변수로 들어온 to는 곧 이메일 주소.
    //MimeMessage 객체 안에 전송할 메일의 내용을 담는다.
    //bean으로 등록해둔 javaMail 객체를 사용해서 이메일 보낸다.
    //0이면 회원가입 관련 이메일
    //1이면 비밀 번호 재설정 이메일
    @Transactional
    public ErrorResponse sendSimpleMessage(EmailReqest emailReqest) throws Exception{
        String title="";
        String s="";
        int n = 0; //인증코드 자릿수
        switch(emailReqest.getNum()){
            case 0:
                title = "All Form U 회원가입 이메일 인증";
                s = "회원가입 인증 코드입니다.";
                n = 6;
                break;
            case 1:
                title="All Form U 비밀번호 재설정";
                s ="새로 설정된 임시 비밀번호 입니다.";
                n = 8;
                break;
        }
        MimeMessage message = createMessage(emailReqest.getEmail(), n, title, s);
        try{
            javaMailSender.send(message); //메일 발송
        }catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException("Failed to send email", e);
        }
        if(emailReqest.getNum()==1){
            Member member;
            List<Member> memberList = memberRepository.findMembersByEmail(emailReqest.getEmail());
            for(Member m : memberList) {
                if(m.getStatus() == Status.STATUS_MEMBER){
                    member = m;
                    member.UpdateMemberPassword(authNum);
                    log.info("임시 비번"+ member.getPassword());
                    break;
                }
                else{
                    throw new NotFoundMemberByEmailException();
                }
            }
        }
        return ErrorResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(authNum)
                .build(); //메일로 보냈던 인증 코드 서버로 리턴
    }


}
