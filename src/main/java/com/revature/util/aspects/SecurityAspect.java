package com.revature.util.aspects;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.revature.dtos.Principal;
import com.revature.util.Secured;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class SecurityAspect {

    @Autowired HttpServletRequest request;
    
    // Custom make exceptions
    @Around("@annotation(com.revature.quizzard.web.security.Secured)")
    public Object secureEndpoint(ProceedingJoinPoint pjp) throws Throwable {
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        Secured securedAnno = method.getAnnotation(Secured.class);

        List<String> allowedRoles = Arrays.asList(securedAnno.allowedRoles());
        Principal principal = (Principal) request.getSession().getAttribute("principal");

        if (principal == null) {
            throw new AuthenticationException();
        }
        if (!allowedRoles.contains(principal.getRole())) {
            throw new AuthorizationException();
        }
        // pjp.proceed() allows method to proceed
        return pjp.proceed();
        // Could do something after methodd runs if wanted

    }
}
