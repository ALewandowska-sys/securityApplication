package com.springSecurity.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;

public class JwtFilter implements Filter {

    private final String secret;

    @Autowired
    public JwtFilter(Environment env) {
        this.secret = env.getRequiredProperty("jwt.secret");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String header = httpServletRequest.getHeader("authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new ServletException("Missing or invalid Authorization header");
        }
        else {
            try {
                String token = header.substring(7);
                Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
                servletRequest.setAttribute("claims", claims);
                if(claims.getExpiration().before(new Date())) {

                    //TODO: Add two options if token isn't active:
                    // - logout (remove token from database)
                    // - stay on website, so create refresh token
                    // - before 15s automatically go to log out

                    System.out.println("token is expired");
                }
            } catch (SignatureException e) {
                throw new ServletException("Invalid token");
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
