package org.springboot.demo.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.subject.WebSubject;
import org.joda.time.DateTime;
import org.springboot.demo.common.cost.JWTCost;
import org.springframework.http.HttpStatus;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTFilter extends BasicHttpAuthenticationFilter {


    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
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

                PrincipalCollection principals = new SimplePrincipalCollection(body.getId(), "authorizingRealm");//拼装shiro用户信息
                WebSubject.Builder builder = new WebSubject.Builder(request, response);
                builder.principals(principals);
                builder.authenticated(true);
                WebSubject subject = builder.buildWebSubject();
                //塞入容器，统一调用
                ThreadContext.bind(subject);
                return true;
            }
        } else {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
        return false;
    }
}