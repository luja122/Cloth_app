package com.spring.CloathingStore.security;

import com.spring.CloathingStore.model.Users;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Service
@Data
public class JwtSecurity {
     @Value("${security.jwt.secretkey}")
     private String secretKey;
     @Value("${security.jwt.access-ttl}")
    private long access_ttl;
     @Value("${security.jwt.issuer}")
    private String issuer;
     @Value("${security.jwt.refresh-ttl}")
    private long refresh_ttl;
     private SecretKey key;
     @PostConstruct
    public void init(){
         this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
     }
     public String generateAccessToken(Users user){
         Instant now = Instant.now();
     return Jwts.builder().setId(UUID.randomUUID().toString())
             .setIssuer(issuer)
             .setSubject(user.getId().toString())
             .setIssuedAt(Date.from(now))
             .setExpiration(Date.from(now.plusMillis(access_ttl)))
             .addClaims(
                     Map.of(
                             "email",user.getEmail(),
                             "role", user.getRole(),
                             "typ","acesstoken"

                     ))
             .signWith(key, SignatureAlgorithm.HS512)
             .compact();
     }
        public String generateRefreshToken(Users user, String jti){
            Instant now = Instant.now();
            return Jwts.builder().setId(jti)
                    .setIssuer(issuer)
                    .setSubject(user.getId().toString())
                    .setIssuedAt(Date.from(now))
                    .setExpiration(Date.from(now.plusMillis(refresh_ttl)))
                    .claim("typ","Refreshtoken")
                    .signWith(key, SignatureAlgorithm.HS512)
                    .compact();
        }
        public Jws<Claims> prase(String token){
         try{
             return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
         }catch (Exception e){
             System.out.println(e.getMessage());
             throw e;
         }
        }
        public boolean isAccessToken(String token){
       Claims c = prase(token).getBody();
       return "accesstoken".equals(c.get("typ"));
        }
    public boolean isRefreshToken(String token){
        Claims c = prase(token).getBody();
        return "refreshtoken".equals(c.get("typ"));
    }
    public String userId(String token){
         Claims c = prase(token).getBody();
         return c.getSubject();
    }
    public String getJti(String token){
         Claims c = prase(token).getBody();
         return c.getId();
    }
    public String getEmail(String token){
         Claims c = prase(token).getBody();
         return c.get("email").toString();
    }
    public String getRole(String token){
         Claims c= prase(token).getBody();
         return c.get("role").toString();
    }


}
