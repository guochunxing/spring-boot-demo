package org.springboot.demo.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.springboot.demo.common.cost.JWTCost;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                String[] roles = body.getSubject().split(",");
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                Arrays.asList(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
                //判断是否更新token
                DateTime expiration = new DateTime(body.getExpiration());
                Duration duration = new Duration(DateTime.now(), expiration);
                long millis = duration.getMillis();
                long tenMinutes = 1000 * 60 * 10;
                if (millis < tenMinutes && millis > 0) {
                    body.setExpiration(DateTime.now().plusMinutes(30).toDate());
                    String updateToken = Jwts.builder().setClaims(body).compact();
                    response.addHeader("Authorization", "Bearer " + updateToken);
                } else {
                    response.addHeader("Authorization", "Bearer " + token);
                }
                return new UsernamePasswordAuthenticationToken(body.getIssuer(), body.getId(), authorities);
            }
            return null;
        }
        return null;
    }
}
