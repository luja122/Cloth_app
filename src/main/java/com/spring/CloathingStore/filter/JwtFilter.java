package com.spring.CloathingStore.filter;

import com.spring.CloathingStore.helper.UserHelper;
import com.spring.CloathingStore.model.Users;
import com.spring.CloathingStore.repo.UserRepo;
import com.spring.CloathingStore.security.JwtSecurity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Security;
import java.util.List;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private  JwtSecurity jwtSecurity;
    @Autowired
    private UserHelper helper;

    private final UserRepo userRepo;
    //this filter will not run for auth like register and login because they don't have jwt token
@Override
protected boolean shouldNotFilter(HttpServletRequest request){
    return request.getServletPath().startsWith("/auth");
}
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
   String header = request.getHeader("Authorization");
   if(header==null || !header.startsWith("Bearer ")){
       filterChain.doFilter(request, response);
       return;
   }
   try{
       String token = header.substring(7);
       if(!jwtSecurity.isAccessToken(token)){
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          return;
       }
     Jws<Claims> claims= jwtSecurity.prase(token);
    String id = claims.getBody().getSubject();
       UUID userId = helper.praseUuid(id) ;
       Users user = userRepo.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found"));
       if(!user.isEnable()){
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
       return;
       }
       if(SecurityContextHolder.getContext().getAuthentication()==null){
           List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
           UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user,"",authorities);
           auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
           SecurityContextHolder.getContext().setAuthentication(auth);
       }


   }catch (Exception e){
       response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
       response.getWriter().write("Invalid or expired token");
return;
   }



        filterChain.doFilter(request, response);
    }


}
