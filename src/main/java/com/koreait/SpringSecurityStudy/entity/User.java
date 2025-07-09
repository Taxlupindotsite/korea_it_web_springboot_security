package com.koreait.SpringSecurityStudy.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class User {

    private Integer userId;
    private String username;
    @JsonIgnore
    private String password;
    private String email;

//  Entity의 정의로 따지면 위배되는가?
//  JPA를 사용하면 위배될수 있으나 Mybatis에서는 엔티티가 DTO개념을
//  같이 갖고 있기때문에 가능

    private List<UserRole> userRoles;

}
