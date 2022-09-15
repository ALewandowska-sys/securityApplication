package com.springSecurity.service;

import com.springSecurity.token.TokenModel;
import com.springSecurity.user.UserModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("ADMIN")
    public List<UserModel> getUsers(){
        return service.getUsers();
    }

    @PostMapping
    public void saveUser(@RequestBody UserModel user){
        service.saveUser(user);
    }

    @GetMapping("/token")
    public String finishRegister(@RequestParam String token){
        return service.compareToken(token);
    }

    @GetMapping("/logout")
    public void logout(){
        // TODO: delete token for user
    }

    @PostMapping("/login")
    public void login(@RequestParam String name, @RequestParam String password){
        // TODO: create token if user is present inside repo
    }
}
