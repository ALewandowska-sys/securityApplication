package com.springSecurity.test;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/application")
public class TestController {

    //TODO: Change static String to real get role

    @GetMapping("/forAdmin")
    @PreAuthorize("ADMIN")
    public String getAdminRole(){
        return "Admin";
    }

    @GetMapping("/forManager")
    @PreAuthorize("MANAGER")
    public String getManagerRole(){
        return "Manager";
    }

    @GetMapping("/forUser")
    @PreAuthorize("USER")
    public String getUserRole(){
        return "User";
    }


}
