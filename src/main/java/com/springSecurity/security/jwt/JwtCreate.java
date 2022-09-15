package com.springSecurity.security.jwt;

import com.springSecurity.user.UserModel;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class JwtCreate {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.time}")
    private long milliS;

    public String generateJWT(UserModel user) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + milliS))
                .setSubject(user.getUsername())
                .claim("role", user.getRole())
                .signWith(signatureAlgorithm, signingKey)
                .compact();
    }
}
