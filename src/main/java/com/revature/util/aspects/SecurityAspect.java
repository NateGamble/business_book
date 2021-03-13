package com.revature.util.aspects;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.revature.dtos.Principal;
import com.revature.exceptions.AuthenticationException;
import com.revature.exceptions.AuthorizationException;
import com.revature.util.JwtConfig;
import com.revature.util.Secured;
import com.revature.util.JwtParser;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
public class SecurityAspect {

    private HttpServletRequest request;
    private final JwtParser jwtParser;

    @Autowired
    public SecurityAspect(HttpServletRequest request, JwtParser jwtParser) {
        this.request = request;
        this.jwtParser = jwtParser;
    }


    // Custom make exceptions
    @Around("@annotation(com.revature.util.Secured)")
    public Object secureEndpoint(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Secured securedAnno = method.getAnnotation(Secured.class);

        List<String> allowedRoles = Arrays.asList(securedAnno.allowedRoles());

        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new AuthenticationException("An unauthenticated request was made to a protected endpoint!");
        }

        String token = Stream.of(cookies)
                        .filter(c -> c.getName().equals("bb-token"))
                        .findFirst()
                        .orElseThrow(AuthenticationException::new)
                        .getValue();

        Principal principal = jwtParser.parseToken(token);
        System.out.println(principal.getRole());
        //Principal principal = (Principal) request.getAttribute("principal");

        if (principal == null) {
            throw new AuthenticationException();
        }
        if (!allowedRoles.contains(principal.getRole().toString())) {
            System.out.println(principal.getRole());
            throw new AuthorizationException();
        }
        // pjp.proceed() allows method to proceed
        return pjp.proceed();
        // Could do something after methodd runs if wanted

    }
}
