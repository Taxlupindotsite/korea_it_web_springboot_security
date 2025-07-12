package com.koreait.SpringSecurityStudy.service;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

//Spring Security에서 기본으로 제공하는 OAuth2USerService를 상속받아 커스텀함.
public class OAuth2PrincipalUserService extends DefaultOAuth2UserService {

//  OAuth2로 로그인 성공시 호출되는 메서드.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

//  Spirng Security가 OAuth2 provider(구글/네이버)에게 AccessToken으로 사용자 정보를 요청함.
//  그 결과로 받은 사용자 정보(JSON)를 파싱한 객체를 리턴받음.
    OAuth2User oAuth2User = super.loadUser(userRequest);

//  사용자 정보(Map형태)로 추출함.
    Map<String, Object> attributes = oAuth2User.getAttributes();

//  어떤 OAuth2 Provider인지 확인함.
//  provider => 공급처( 구글, 네이버 등)
    String provider = userRequest.getClientRegistration().getRegistrationId();

//  로그인한 사용자의 식별자(id), 이메일을 가져옴

        String email = null;
//        로그인시 사용한 이메일
    String id = null; // 공급처에서 발행한 사용자 식별자

//        provider 종류에 따라 사용자 정보 파싱 방식이 다름.
    switch (provider) {
       case "google":
       id = attributes.get("sub").toString();
       email = (String) attributes.get("email");
       break;
    }
//    우리가 필요한 정보만 골라, 새롭게 attributes를 구성함.
    Map<String, Object> newAttributes = Map.of(
            "id", id,
            "provider" , provider,
            "email",email

    );

// 그리고 권한을 설정. => 임시 권한 부여(ROLE_TEMPORARY)
//  실제 권한은 OAuth2SuccessHandler에서 판단할 것
    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_TEMPORARY"));

//  spring sc가 사용할 ouath2user 객체 생성해서 반환함.
   return new DefaultOAuth2User(authorities, newAttributes, "id");
// 마지막에 들어가는 id는 principal.getName()으로 가져올 때 사용됨.




   }
}
