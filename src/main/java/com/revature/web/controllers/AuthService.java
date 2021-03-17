package com.revature.web.controllers;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.revature.dtos.Credentials;
import com.revature.dtos.Principal;
import com.revature.exceptions.AuthenticationException;
import com.revature.models.Role;
import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.JwtGenerator;
import com.revature.util.JwtParser;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.FilterChain;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import javax.validation.Valid;


@RestController
@RequestMapping("/auth")
public class AuthService {


    private final UserService userService;
    private final JwtGenerator jwtGenerator;
    private final JwtParser jwtParser;

    @Autowired
    public AuthService(UserService userService, JwtGenerator jwtGenerator, JwtParser jwtParser) {
        this.userService = userService;
        this.jwtGenerator = jwtGenerator;
        this.jwtParser = jwtParser;

    }



    /**
     * Parses login information and gives a token for a valid user
     * @param credentials the credentials from json
     * @param response the http response to attach the cookie to
     * @return a Principal object containing basic user information and a web token
     */
    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Principal authenticateUser(@RequestBody @Valid Credentials credentials, HttpServletResponse response) {
        Principal principal = authenticate(credentials.getUsername(), credentials.getPassword());

        Cookie cookie = new Cookie("bb-token", principal.getToken());
        cookie.setPath("/");
        response.addCookie(cookie);
        response.setHeader("Set-Cookie", "key=" + cookie.getValue() + "; SameSite=strict");
        return principal;
    }

    /**
     * Authenticates user and generates a token
     * @param username username of user to validate
     * @param password password of user to validate
     * @return a Principal object containing basic user information and a web token
     */
    public Principal authenticate(String username, String password) {

        try {

            // Getting the User with the hashed password from the database
            User hashedUser = userService.getUserByUsername(username);

            // Creating a BCrypt result object to check if the given password matches the hashed password
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), hashedUser.getPassword());

            // Creating a Principal of the hashed User
            Principal principal = new Principal(hashedUser);

            // If the result is true
            if (result.verified) {

                // Generate a JWT from the Principal
                String token = jwtGenerator.createJwt(principal);

                // Set the Principal's token
                principal.setToken(token);

            } else {

                // If the result is false, throw not confirmed
                throw new AuthenticationException("Account not confirmed.");
            }

            return principal;

        }catch(AuthenticationException e){

            throw new AuthenticationException("Account not confirmed.");
        }
    }


    /**
     * Parses a token and gets the embedded role
     * @param token the token to be parsed
     * @return the role inside the token
     */
    public Role getTokenAuthorities(String token) {

        // Creates a Principal from the given token
        Principal principal = jwtParser.parseToken(token);
        
        if (principal == null) {
            throw new RuntimeException("Principal within token was null!");
        }

        return principal.getRole();
    }







}
