package gak.backend.domain.member.application;

import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class RegisterMailService{
    private final JavaMailSender javaMailSender;

    //인증번호 생성
    private final String authNum = createKey();
    private String id;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException, jakarta.mail.MessagingException {
        log.info("보내는 대상" + to);
        log.info("인증 번호" + authNum);
        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(Message.RecipientType.TO, to); //보내는 대상
        message.setSubject("All Form U 회원가입 이메일 인증"); //제목

        String msg = "";
        msg += "<div style='margin:100px;'>";
        msg += "<h1>안녕하세요</h1>";
        msg += "<h3>너를 위해서, 너만을 위해서<h3>";
        msg += "<h1>당신을 위한 설문 플랫폼 All Form U입니다.";
        msg += "<br>";
        msg += "<p>만족스러운 서비스를 제공하도록 노력하겠습니다. 감사합니다!";
        msg += "<div aling='center' style='border:1px solid black; font-family:verdana';>";
        msg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msg += "<div style='font-size:130%'>";
        msg += "CODE : <strong>";
        msg += authNum + "</strong><div><br/>"; //메일에 인증번호 넣기
        msg += "</div>";
        message.setFrom(new InternetAddress("allformu@naver.com", "All_Form_U")); //보내는 사람

        return message;
    }

    //인증코드 전송
    public String createKey(){
        StringBuffer key = new StringBuffer();
        Random rand = new Random();

        for(int i = 0; i < 8; i++) {//인증코드 6자리
            int index = rand.nextInt(3); // 0~2까지 랜덤, rand 값에 따라서 아래 switch문이 실행.

            switch (index) {
                case 0:
                    key.append((char) ((int) (rand.nextInt(26)) + 97));
                    //a-z (ex. 1+97 => 'b')
                    break;
                case 1:
                    key.append((char) ((int) (rand.nextInt(26)) + 65));
                    //A-Z
                    break;
                case 2:
                    key.append((rand.nextInt(10)));
                    //0-9
                    break;
            }
        }
        return key.toString();
    }


    //메일 발송
    //senSimpleMessage의 매개변수로 들어온 to는 곧 이메일 주소.
    //MimeMessage 객체 안에 전송할 메일의 내용을 담는다.
    //bean으로 등록해둔 javaMail 객체를 사용해서 이메일 보낸다.
    public String sendSimpleMessage(String to) throws Exception{
        MimeMessage message = createMessage(to);
        try{
            javaMailSender.send(message); //메일 발송
        }catch (MailException e){
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
        return authNum; //메일로 보냈던 인증 코드 서버로 리턴
    }

}
