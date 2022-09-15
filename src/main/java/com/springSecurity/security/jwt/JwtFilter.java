package com.springSecurity.security.jwt;

import com.springSecurity.token.TokenModel;
import com.springSecurity.token.TokenRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

@Component
public class JwtFilter implements Filter {

    private final String secret;
    @Autowired
    private TokenRepo tokenRepo;

    @Autowired
    public JwtFilter(Environment env) {
        this.secret = env.getRequiredProperty("jwt.secret");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String header = httpServletRequest.getHeader("authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header");
        }
        else {
            checkToken(header, (HttpServletRequest) servletRequest);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void checkToken(String header, HttpServletRequest servletRequest) throws ServletException {
        try {
            String token = header.substring(7);
            Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
            servletRequest.setAttribute("claims", claims);
            if(claims.getExpiration().before(new Date())) {
                handleExpirationToken(token);
            }
        } catch (SignatureException e) {
            throw new ServletException("Invalid token");
        }
    }

    private void handleExpirationToken(String token) {
        createTimer(token);

        Scanner scanner =new Scanner(System.in);
        System.out.print("Enter your choice (1- logout, 2- stay): ");
        int choice = scanner.nextInt();

        if(choice == 1){
            refreshToken();
        } else {
            logout(token);
        }
    }

    private void createTimer(String token) {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                logout(token);
                timer.cancel();
            }
        };
        timer.schedule(task, 15000);
    }

    private void refreshToken() {
        //TODO: create refreshToken
    }

    private void logout(String token) {
        Optional<TokenModel> tokenModel = tokenRepo.findByValue(token);
        tokenModel.ifPresent(model -> tokenRepo.delete(model));
        System.out.println("You have to login again");
    }
}
