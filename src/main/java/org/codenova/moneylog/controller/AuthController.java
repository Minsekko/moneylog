package org.codenova.moneylog.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.moneylog.entity.User;
import org.codenova.moneylog.request.LoginRequest;
import org.codenova.moneylog.service.KakaoApiService;
import org.codenova.moneylog.service.NaverApiService;
import org.codenova.moneylog.vo.KakoTokenResponse;
import org.codenova.moneylog.repository.UserRepository;
import org.codenova.moneylog.vo.NaverProfileResponse;
import org.codenova.moneylog.vo.NaverTokenResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@Slf4j
@AllArgsConstructor
public class AuthController {

    private KakaoApiService kakaoApiService;
    private NaverApiService naverApiService;
    private UserRepository userRepository;

    @GetMapping("/login")
    public String loginHandle(Model model) {
        log.info("longinHandle...executed");
        model.addAttribute("kakaoClientId", "87b9b260d6986054d5a26cd8e385e653");
        model.addAttribute("kakaoRedirectUri", "http://localhost:8080/auth/kakao/callback");

        model.addAttribute("naverClientId", "YsAytQL_JngfBpZjm9Wf"); //naver의 요청값 전송 하고 받는다
        model.addAttribute("naverRedirectUri", "http://192.168.10.40:8080/auth/naver/callback");
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginPostHandle(@ModelAttribute LoginRequest loginRequest,
                                  HttpSession session,
                                  Model model) {
        //log.info("longinHandle...executed");
        User user = userRepository.findByEmail(loginRequest.getEmail());
        if(user != null && user.getPassword().equals(loginRequest.getPassword())) {
            session.setAttribute("user",user);
            return "redirect:/index";
        } else {
            return "redirect:/auth/login";
        }
    }

    @GetMapping("/signup")
    public String signupHandle(Model model) {

        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupPostHandle(@ModelAttribute User user) {
        User found = userRepository.findByEmail(user.getEmail());

        if (found == null) {
            user.setProvider("LOCAL");
            user.setVerified("F");
            userRepository.save(user);
        }
        return "redirect:/index";
    }


    @GetMapping("/kakao/callback")
    public String kakaoCallbackHandle(@RequestParam("code") String code, HttpSession session) throws JsonProcessingException {
        //log.info("code = {}",code);
        KakoTokenResponse response = kakaoApiService.exchangeToken(code);
//        log.info("response.idToken = {}", response.getIdToken());
        DecodedJWT decodedJWT = JWT.decode(response.getIdToken());
        String sub = decodedJWT.getClaim("sub").asString();
        String nickname = decodedJWT.getClaim("nickname").asString();
        String picture = decodedJWT.getClaim("picture").asString();

        log.info("decodedJWT:sub={},nickname= {}, picture={}", sub, nickname, picture);

        User found = userRepository.findByProviderAndProviderId("KAKAO", sub);
        log.info("found = {}", found);
        if(found != null) {
            session.setAttribute("user", found);
        } else {
            User user = User.builder().provider("KAKAO")
                    .providerId(sub).nickname(nickname).picture(picture).verified("T").build();
            userRepository.save(user);
            session.setAttribute("user", user);
        }

        return "redirect:/";
    }

    @GetMapping("/naver/callback")
    public String naverCallbackHandle(@RequestParam("code") String code,
                                      @RequestParam String state,
                                      HttpSession session) throws JsonProcessingException {
        log.info("code={},state ={}", code, state);// 이 응답을 객체로 파싱해야 한다 Vo를 만든다
        NaverTokenResponse tokenResponse = naverApiService.exchangeToken(code,state);

        log.info("accessToken ={}", tokenResponse.getAccesstoken());

        NaverProfileResponse profileResponse
                = naverApiService.exchangeProfile(tokenResponse.getAccesstoken());

        log.info("profileResponse id = {}" , profileResponse.getId());
        log.info("profileResponse nickname = {}" , profileResponse.getNickname());
        log.info("profileResponse ProfileImage = {}" , profileResponse.getProfileImage());

        User found = userRepository.findByProviderAndProviderId("NAVER", profileResponse.getId());

        if(found == null) {
            User user = User.builder()
                            .nickname(profileResponse.getNickname())
                            .provider("NAVER")
                            .providerId(profileResponse.getId())
                            .verified("T")
                            .picture(profileResponse.getProfileImage()).build();
            userRepository.save(user);

        } else {
            session.setAttribute("user", found);
        }
        return "redirect:/index";
    }

}
