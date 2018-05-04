package org.springboot.demo.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.joda.time.DateTime;
import org.springboot.demo.common.cost.JWTCost;
import org.springboot.demo.security.authentication.CustomAuthenticationProvider;
import org.springboot.demo.utils.ContextUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * token的校验
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request, response);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) {
        String token = request.getHeader("Authorization");
        if (token != null) {
            // parse the token.
            Claims body = Jwts.parser()
                    .setSigningKey(JWTCost.signatureKey)
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody();
            if (body != null) {
                String userId = body.getId();
                body.setExpiration(DateTime.now().plusMinutes(30).toDate());
                String updateToken = Jwts.builder().setClaims(body).compact();
                response.addHeader("Authorization", "Bearer " + updateToken);
                return new UsernamePasswordAuthenticationToken(userId, null, ContextUtil.getBean(CustomAuthenticationProvider.class).authorize(userId));
            }
            return null;
        }
        return null;
    }
}
