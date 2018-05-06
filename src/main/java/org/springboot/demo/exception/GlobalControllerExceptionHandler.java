package org.springboot.demo.exception;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.UnauthorizedException;
import org.springboot.demo.utils.LogUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    public Object allExceptionHandler(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        String message = exception.getCause().getMessage();
        LogUtil.error(message);
        return new ResultInfo(exception.getClass().getName(), message);
    }

    /*=========== Shiro 异常拦截==============*/

    @ExceptionHandler(value = IncorrectCredentialsException.class)
    public String IncorrectCredentialsException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "IncorrectCredentialsException";
    }

    @ExceptionHandler(value = UnknownAccountException.class)
    public String UnknownAccountException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "UnknownAccountException";
    }

    @ExceptionHandler(value = LockedAccountException.class)
    public String LockedAccountException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "LockedAccountException";
    }

    @ExceptionHandler(value = ExcessiveAttemptsException.class)
    public String ExcessiveAttemptsException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "ExcessiveAttemptsException";
    }

    @ExceptionHandler(value = AuthenticationException.class)
    public String AuthenticationException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "AuthenticationException";
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public String UnauthorizedException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        return "UnauthorizedException";
    }
}