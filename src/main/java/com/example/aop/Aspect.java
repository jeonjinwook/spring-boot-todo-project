package com.example.aop;

import com.example.cmn.UserCmnDto;
import com.example.demo.user.dto.Users;
import com.example.demo.user.repository.UserRepository;
import com.example.jwt.JwtUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@org.aspectj.lang.annotation.Aspect
@Component
public class Aspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public Aspect(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Pointcut("execution(public * com.example.demo.*.controller.*.*(..))")
    private void getCmnController() { }

    @Around("getCmnController()")
    public Object action(ProceedingJoinPoint joinPoint) throws Throwable {
        setUserCmnData(joinPoint);

        Object result = joinPoint.proceed();


        return result;
    }
    private void setUserCmnData(ProceedingJoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Users user = getRequestUsers(request);

        if (user != null) {

            for (Object arg : args) {

                if (arg instanceof UserCmnDto) {

                    ((UserCmnDto) arg).setUserNo(user.getUserNo());

                }
            }

        }

    }

    private Users getRequestUsers(HttpServletRequest request) {

        String token = request.getHeader("Authorization");

        if (token == null || token.equals("null")) {
            return null;
        }

        String userName = jwtUtil.extractUsername(token.substring(7));

        return userRepository.findByEmail(userName).get();
    }

}
