package com.springSecurity.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/application")
public class TestController {

    //TODO: Change static String to real get role

    @GetMapping("/forAdmin")
    public String getAdminRole(){
        return "Admin";
    }

    @GetMapping("/forManager")
    public String getManagerRole(){
        return "Manager";
    }

    @GetMapping("/forUser")
    public String getUserRole(){
        return "User";
    }


}
