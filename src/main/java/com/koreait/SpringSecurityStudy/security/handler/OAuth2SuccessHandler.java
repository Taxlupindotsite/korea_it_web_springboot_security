package com.koreait.SpringSecurityStudy.security.handler;

import com.koreait.SpringSecurityStudy.entity.OAuth2User;
import com.koreait.SpringSecurityStudy.entity.User;
import com.koreait.SpringSecurityStudy.repository.OAuth2UserRepository;
import com.koreait.SpringSecurityStudy.repository.UserRepository;
import com.koreait.SpringSecurityStudy.security.jwt.JwtUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

// 사용자 로그인에 동의햇고, 정보에 파싱했을때,
// 이 정보를 어떻게 처리할지 이 클래스에서 정의
@Component

public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private OAuth2UserRepository oAuth2UserRepository;
//    DB확인할떄 사용

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
  // OAuth2User 정보 가져오기.
  DefaultOAuth2User defaultOAuth2User = (DefaultOAuth2User) authentication.getPrincipal();
  String provider = defaultOAuth2User.getAttribute("provider");
  String providerUserId = defaultOAuth2User.getAttribute("id");
  String email = defaultOAuth2User.getAttribute("email");

  // provider, providerUserId로 이미 연동된 사용자 정보가 있는지 DB를 조회함

  OAuth2User oAuth2User = oAuth2UserRepository.getOAuth2UserByProviderAndProviderUserId(provider, providerUserId);

//  OAuth2 로그인을 통해 회원가입이 되어있지 않거나, 아직 연동되지 않은 상태
  if(oAuth2User == null) {
// 프론트로 provider와 providerUserId, email을 전달
  response.sendRedirect("http://localhost:3000/auth/oauth2?provider=" + provider +"&providerUserID" + providerUserId + "&email=" + email);
// 리액트로 보낸다는 가정하에 3000번
  return;
  }

  // 연동된 사용자가 있다면? =? userId를 통해 회원 정보를 조회함.
  Optional<User> optionalUser = userRepository.getUserByUserId(oAuth2User.getUserId());

  //OAuth2 로그인을 통해 회원가입이나 연동을 진행한 경우
  String accessToken = null;
  if(optionalUser.isPresent()){
   accessToken = jwtUtil.generateAccessToken(Integer.toString(optionalUser.get().getUserId()));

  }
//    최종적으로 accessToken을 쿼리 파라미터로 프론트에 전달.
        response.sendRedirect("http://localhost:3000/auth/oauth2/signin?accessToken=" + accessToken);

  }

}

