package com.springSecurity.user;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ApiModel
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String password;
    @Column(unique = true)
    private String username;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;
    private boolean isEnable = false;
}
