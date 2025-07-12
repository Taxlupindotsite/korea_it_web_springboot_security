package com.koreait.SpringSecurityStudy.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder

public class OAuth2User {
    private Integer ouath2Id;
    private Integer userId;
    private String provider;
    private String providerUserId;
    private LocalDateTime createDt;
    private LocalDateTime updateDt;

}
