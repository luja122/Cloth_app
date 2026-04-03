package com.spring.CloathingStore.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {
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


   }catch (){

   }



        filterChain.doFilter(request, response);
    }


}
