package org.springboot.demo.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.subject.WebSubject;
import org.joda.time.DateTime;
import org.springboot.demo.common.cost.JWTCost;
import org.springframework.http.HttpStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String authenticationHeader = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isNotEmpty(authenticationHeader)) {
            Claims body = Jwts.parser()
                    .setSigningKey(JWTCost.signatureKey)
                    .parseClaimsJws(authenticationHeader)
                    .getBody();
            if (body != null) {
                //更新token
                body.setExpiration(DateTime.now().plusMinutes(30).toDate());
                String updateToken = Jwts.builder().setClaims(body).compact();
                httpServletResponse.addHeader(AUTHORIZATION_HEADER, updateToken);

                PrincipalCollection principals = new SimplePrincipalCollection(body.getId(), "tokenRealm");//拼装shiro用户信息
                WebSubject.Builder builder = new WebSubject.Builder(servletRequest, servletResponse);
                builder.principals(principals);
                builder.authenticated(true);
                WebSubject subject = builder.buildWebSubject();
                //塞入容器，统一调用
                ThreadContext.bind(subject);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } else {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
        }
    }
}