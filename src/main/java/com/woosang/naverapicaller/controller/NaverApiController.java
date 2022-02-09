package com.woosang.naverapicaller.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.woosang.naverapicaller.utils.Signatures;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;
import java.time.Instant;
import java.util.*;

@RestController
public class NaverApiController {


    @RequestMapping("/**")
    public Map callNaver(HttpServletRequest httpServletRequest,
                                 @RequestHeader("X-API-KEY") String key,
                                 @RequestHeader("X-API-SECRET") String secret,
                                 @RequestHeader("X-Customer") String customer) throws SignatureException, JsonProcessingException {

        String requestURI = httpServletRequest.getRequestURI();

        // Parameter 그대로 가져오기
        Set<String> strings = httpServletRequest.getParameterMap().keySet();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        strings.forEach(k -> params.add(k, httpServletRequest.getParameter(k).replaceAll(" ", "")));

        // Signature 만들기
        String timestamp = String.valueOf(Instant.now().toEpochMilli());
        String signature = Signatures.of(timestamp, httpServletRequest.getMethod(), requestURI, secret);

        WebClient client = WebClient.builder()
                .baseUrl("https://api.naver.com")
                .defaultHeader("X-API-KEY", key)
                .defaultHeader("X-Customer", customer)
                .defaultHeader("X-Timestamp", timestamp)
                .defaultHeader("X-Signature", signature)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        Mono<String> request = client.get()
                .uri(uriBuilder -> uriBuilder.path(requestURI)
                .queryParams(params)
                .build())
                .retrieve()
                .bodyToMono(String.class);

        return new ObjectMapper().readValue(request.block(), Map.class);
    }
}
