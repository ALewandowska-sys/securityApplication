package com.springSecurity.security;

import com.springSecurity.user.UserModel;
import com.springSecurity.user.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service("UserService")
public class UserSecurityService implements UserDetailsService {
    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final UserModel customer = repo.findByUsername(username);
        if(customer == null){
            throw new UsernameNotFoundException(username);
        }

        return User
                .withUsername(customer.getUsername())
                .password(customer.getPassword())
                .roles(customer.getRole().name())
                .build();
    }
}
