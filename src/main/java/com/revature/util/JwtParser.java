package com.revature.util;

import com.revature.dtos.Principal;
import com.revature.models.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.InputStream;
import java.util.Properties;
@Component
public class JwtParser {

    private JwtConfig config;

    @Autowired
    public JwtParser(JwtConfig config) {
        this.config = config;
    }

    public Principal parseToken(String token) {

        Claims claims = Jwts.parser()
                .setSigningKey(config.getSigningKey())
                .parseClaimsJws(token)
                .getBody();

        int id = Integer.parseInt(claims.getId());
        String username = claims.getSubject();
        Role role = claims.get("role", Role.class);
        return new Principal(id, username, role);

    }


}
