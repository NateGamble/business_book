package com.revature.util;

import com.revature.dtos.Principal;
import com.revature.models.User;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;


@Component
public class JwtGenerator {

    private JwtConfig config;

    @Autowired
    public JwtGenerator(JwtConfig config) {
        this.config = config;
    }

    public String createJwt(Principal subject) {

        long now = System.currentTimeMillis();

        JwtBuilder builder = Jwts.builder()
                .setId(String.valueOf(subject.getId()))
                .setSubject(subject.getUsername())
                .claim("role", subject.getRole())
                .setIssuer("revature")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + (60 * 60 * 1000)))
                .signWith(config.getSignatureAlgorithm(), config.getSigningKey());

        return builder.compact();

    }
}
