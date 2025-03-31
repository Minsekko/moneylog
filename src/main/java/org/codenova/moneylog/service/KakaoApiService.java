package org.codenova.moneylog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.moneylog.vo.KakoTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class KakaoApiService {

    private ObjectMapper objectMapper;

    public KakoTokenResponse exchangeToken(String code) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Contents-Type", "application/x-www-urlencoded;charset=utf-8");

        MultiValueMap<String,String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","authorization_code");
        body.add("client_id","87b9b260d6986054d5a26cd8e385e653");
        body.add("redirect_uri","http://localhost:8080/auth/kakao/callback");
        body.add("code",code);

        ResponseEntity<String> response = restTemplate.exchange("https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                new HttpEntity<>(body,headers),
                String.class
        );

        log.info("kakao API response: {}", response.getBody());
        KakoTokenResponse kakoTokenResponse =
            objectMapper.readValue(response.getBody(), KakoTokenResponse.class);
        return kakoTokenResponse;
    }
}
