package org.codenova.moneylog.service;

import jakarta.mail.internet.MimeMessage;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.moneylog.entity.User;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service //컨트롤러에서 사용하기 위함
@AllArgsConstructor
@Slf4j
public class MailService {

    private JavaMailSender mailSender;
    //private MailSender mailSender;

    public boolean sendWelcomeMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("[코드노바] 머니로그 회원이 되신걸 환영합니다");
        message.setText("안녕하세요," + user.getNickname()+ "님!\n 머니로그에 가입해 주셔서 감사합니다.\n\n 앞으로 다양한 컨텐츠와 서비스를 제공해 드리겠습니다. \n\n 팀 코드노바 드림");

        boolean result = true;
        try {
            mailSender.send(message);
            result = true;
        } catch (Exception e) {
            log.error("error = {}", e);

            result = false;
        }

        return result;
    }

    public boolean SendWelcomeHtmlMessage(User user) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(message, "utf-8");
            mimeMessageHelper.setTo(user.getEmail());
            mimeMessageHelper.setSubject("[코드노바] 머니로그 회원이 되신걸 환영합니다");

            String html ="<h2>안녕하세요, 머니로그입니다</h2>";
            html += "<p><a href='http://192.168.10.40:8080'>머니로그</a>에 가입하신것을 진심으로 환영합니다.<p>";
            html += "<p>앞으로 다양한 컨텐츠와 서비스를 제공해드리겠습니다.<p>";
            html += "<p><span style='color : gray'>팀 코드노바 올림 </span><p>";
            mimeMessageHelper.setText(html,true);
            mailSender.send(message);

        }catch (Exception e) {
            log.error("error ={}", e);
        }
        return true;
    }

    public boolean sendTemporalPasswordMessage(String email, String temporalPassword) {

        SimpleMailMessage message = new SimpleMailMessage(); //메세지 줄 타입 선언

        message.setTo(email);
        message.setSubject("[코드노바] 임시 비밀 번호를 발급해 드립니다");
        message.setText("안녕하세요, 새로 생성된 임시 비밀번호는" + temporalPassword + "입니다. \n 임시번호로 로그인 후 비밀번호를 재성성 하시기 바랍니다. \n\n 팀 코드노바 드림");

        boolean result = true;
        try {
            mailSender.send(message);
            result = true;
        } catch (Exception e) {
            log.error("error = {}", e);

            result = false;
        }
        return result;
    }
}
