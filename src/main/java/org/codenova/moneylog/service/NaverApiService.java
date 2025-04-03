package org.codenova.moneylog.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codenova.moneylog.vo.NaverProfileResponse;
import org.codenova.moneylog.vo.NaverTokenResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@AllArgsConstructor
@Slf4j
public class NaverApiService {

    private ObjectMapper objectMapper; //json 객체로 변환 하는데 사용 하는 라이브러리

    public NaverTokenResponse exchangeToken(String code, String state) throws JsonProcessingException {//네이버에서 엑세스 토큰 가져오기 code,state 두개값을 리턴한걸 이용해서 토큰 요청

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();  //headers 값을 map 설정 하고 받을 준비
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        body.add("grant_type", "authorization_code");
        body.add("client_id", "YsAytQL_JngfBpZjm9Wf");
        body.add("code",code);
        body.add("state",state);
        body.add("client_secret", "D35psGrlJ_"); //네이버 서버에 토큰 달라고 하는 역활이다


        RestTemplate restTemplate = new RestTemplate();  //네이버에 요청을 보내 주는걸 짜야 한다

        ResponseEntity<String> response = restTemplate.exchange("https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                String.class //응답을 json 형태로 받음
        );//네이버 API 요청 보내기

        log.info("response: {}", response.getBody());

        return objectMapper.readValue(response.getBody(), NaverTokenResponse.class);
    }

    public NaverProfileResponse exchangeProfile(String accesstoken) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Authorization", "Bearer " + accesstoken);
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        ResponseEntity<String> response = restTemplate.exchange("https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                new HttpEntity<>(body, headers),
                String.class
        );

        log.info("profile = {}", response.getBody());

        JsonNode root = objectMapper.readTree(response.getBody());
        String extractJson = objectMapper.readTree(response.getBody()).path("response").toString();

        return objectMapper.readValue(extractJson,NaverProfileResponse.class);

    }
}
