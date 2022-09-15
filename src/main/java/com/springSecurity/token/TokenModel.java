package com.springSecurity.token;

import com.springSecurity.user.UserModel;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "token")
@Getter
@Setter
@ApiModel
public class TokenModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserModel user;
}
