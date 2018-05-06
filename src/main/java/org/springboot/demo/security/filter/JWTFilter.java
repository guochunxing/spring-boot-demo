package org.springboot.demo.security.filter;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
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

public class JWTFilter extends BasicHttpAuthenticationFilter {

    /**
     * 自定义执行登录的方法
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        UsernamePasswordToken usernamePasswordToken = JSON.parseObject(httpServletRequest.getInputStream(), UsernamePasswordToken.class);
        // 提交给realm进行登入，如果错误他会抛出异常并被捕获
        Subject subject = this.getSubject(request, response);
        subject.login(usernamePasswordToken);
        return this.onLoginSuccess(usernamePasswordToken, subject, request, response);
        //错误抛出异常
    }

    /**
     * 最先执行的方法
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        return super.preHandle(request, response);
    }

    /**
     * 登录成功后登录的操作
     * 加上jwt 的header
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String jwtToken = Jwts.builder()
                .setId(token.getPrincipal().toString())
                .setExpiration(DateTime.now().plusMinutes(30).toDate())
                .signWith(SignatureAlgorithm.HS256, JWTCost.signatureKey)
                .compact();
        httpServletResponse.addHeader(AUTHORIZATION_HEADER, jwtToken);
        return true;
    }

    /**
     * 登录以及校验的主要流程
     * 判断是否是登录，或者是登陆后普通的一次请求
     */
    @Override
    public void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String servletPath = httpServletRequest.getServletPath();
        if (StringUtils.equals(servletPath, "/login")) {
            //执行登录
            this.executeLogin(servletRequest, servletResponse);
        } else {
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

                    //添加用户凭证
                    PrincipalCollection principals = new SimplePrincipalCollection(body.getId(), JWTCost.UserNamePasswordRealm);//拼装shiro用户信息
                    WebSubject.Builder builder = new WebSubject.Builder(servletRequest, servletResponse);
                    builder.principals(principals);
                    builder.authenticated(true);
                    builder.sessionCreationEnabled(false);
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
}