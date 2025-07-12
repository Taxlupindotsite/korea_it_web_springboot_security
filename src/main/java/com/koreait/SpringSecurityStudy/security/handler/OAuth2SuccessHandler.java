package com.koreait.SpringSecurityStudy.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

// 사용자 로그인에 동의햇고, 정보에 파싱했을때,
// 이 정보를 어떻게 처리할지 이 클래스에서 정의

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
  // OAuth2User 정보 가져오기.
  DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
  String provider = defaultOAuth2User.getAttribute("provider");
  String providerUserId = defaultOAuth2User.getAttribute("id");
  String email = defaultOAuth2User.getAttribute("email");

  // provider, providerUserId로 이미 연동된 사용자 정보가 있는지 DB를 조회함

    }
}

