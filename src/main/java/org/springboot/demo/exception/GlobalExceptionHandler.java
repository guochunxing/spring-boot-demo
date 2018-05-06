package org.springboot.demo.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 截获spring boot Error页面
 */
@RestController
public class GlobalExceptionHandler implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public Object error(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 错误处理逻辑
        Exception exception = (Exception) request.getAttribute("javax.servlet.error.exception");
        Throwable cause = exception.getCause();
        if (cause instanceof ExpiredJwtException) {
            response.setStatus(HttpStatus.GATEWAY_TIMEOUT.value());
            return new ResultInfo("ExpiredJwtException", cause.getMessage());
        }
        if (cause instanceof MalformedJwtException) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return new ResultInfo("MalformedJwtException", cause.getMessage());
        }
        return new ResultInfo(cause.getCause().getMessage(), cause.getMessage());
    }
}
