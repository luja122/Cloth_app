package com.spring.CloathingStore.service;

import com.spring.CloathingStore.model.RefreshToken;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Data
public class CookieService {
    @Value("${security.jwt.refresh-token-cookie-name}")
   private String refreshTokenCookieName;
    @Value("${security.jwt.cookie-http-only}")
    private boolean cookie_Http_Only;
    @Value("${security.jwt.cookie-same-site}")
    private String cookie_Same_Site;
    @Value("${security.jwt.cookie-secure}")
    private boolean cookieSecure;
    @Value("${security.jwt.domain-name}")
    private String domainName;

    public void attachRefreshtokenCookie(HttpServletResponse response,String value, int age){
        var cookie = ResponseCookie.from(refreshTokenCookieName,value)
                .path("/")
                .sameSite(cookie_Same_Site)
                .httpOnly(cookie_Http_Only)
                .maxAge(age)
                .secure(cookieSecure);
                if(domainName!=null || !domainName.isBlank()){
                    cookie.domain(domainName);
                }
               response.addHeader(HttpHeaders.SET_COOKIE,cookie.build().toString());
     }
     public void addNoHeader(HttpServletResponse response){
        response.addHeader(HttpHeaders.CACHE_CONTROL,"no-store");
        response.addHeader("pragma","no-cache");
     }
}
