package com.spring.CloathingStore.service;


import com.spring.CloathingStore.config.UserMapper;
import com.spring.CloathingStore.dtos.LoginRequest;
import com.spring.CloathingStore.dtos.LoginResponse;

import com.spring.CloathingStore.dtos.Userdto;
import com.spring.CloathingStore.exception.RoleNotFoundException;
import com.spring.CloathingStore.exception.UserNotEnable;
import com.spring.CloathingStore.exception.UserNotFoundException;
import com.spring.CloathingStore.model.RefreshToken;
import com.spring.CloathingStore.model.RefreshTokenRequest;
import com.spring.CloathingStore.model.Role;
import com.spring.CloathingStore.model.Users;
import com.spring.CloathingStore.repo.RefreshtokenRepo;
import com.spring.CloathingStore.repo.RoleRepo;
import com.spring.CloathingStore.repo.UserRepo;
import com.spring.CloathingStore.security.JwtSecurity;
import jakarta.servlet.Filter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtSecurity jwtSecurity;
    @Autowired
    private RefreshtokenRepo refreshtokenRepo;
    @Autowired
    private CookieService cookieService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private Filter filter;

    @Override
    public String register(Userdto data) {
       if(!userRepo.existsByGamil(data.getEmail())){

           Users user = new Users();
           user.setEmail(data.getEmail());
           user.setFirstName(data.getLastName());
           Role role = (Role) roleRepo.findByRole("USER").orElseThrow(()-> new RoleNotFoundException("Role not Found "));
           user.setRole(Set.of(role));
           user.setProvider(data.getProvider());
           user.setPassword( passwordEncoder.encode(data.getPassword()));
           userRepo.save(user);
   return "Register Successfully";
       }else{
           return "Register Unsuccessfully";
       }

    }


    @Override
    public LoginResponse login(HttpServletResponse response, LoginRequest data) {
        Authentication auth = new UsernamePasswordAuthenticationToken(data.email(),data.password());
       authenticationManager.authenticate(auth);
       Users user = userRepo.findByEmail(data.email()).orElseThrow(()->new UserNotFoundException("User is not found"));
       if(!user.isEnable()){
 throw new UserNotEnable("User is not Enable/active");
       }
   var refreshtokenobj = RefreshToken.builder().jti(UUID.randomUUID().toString()).user(user).created_At(Instant.now()).expires_At(Instant.now().plusMillis(jwtSecurity.getRefresh_ttl())).revoked(false).build();
       refreshtokenRepo.save(refreshtokenobj);
       String refreshtoken= jwtSecurity.generateRefreshToken(user,refreshtokenobj.getJti());
       String accesstoken = jwtSecurity.generateAccessToken(user);
       cookieService.attachRefreshtokenCookie(response,refreshtoken, (int)jwtSecurity.getRefresh_ttl());
       cookieService.addNoHeader(response);
       return new LoginResponse(refreshtoken,accesstoken,"",jwtSecurity.getAccess_ttl(), userMapper.user_to_UserDto(user));
    }

    @Override
    public Optional<Object> readFromToken(HttpServletRequest request, RefreshTokenRequest body) {
       if(request.getCookies()!=null){
           Optional<String> fromCookie = Arrays.stream(request.getCookies())
                   .filter(cookie -> cookie.getName().equals(cookieService.getRefreshTokenCookieName()))
                   .map(Cookie::getValue)
                   .filter(c->!c.isBlank())
                   .findFirst();
           if(fromCookie.isPresent()){
               return Optional.of(fromCookie);
           }
       }
       //get teh refresh token from the body
        if(body!=null && body.refreshToken()!=null && !body.refreshToken().isBlank()){
            return Optional.of(body.refreshToken());
        }
        //get refresn token from the header
        String authheader = request.getHeader("X-Refreah_Token");
        if(authheader!=null && !authheader.isBlank()){
            return Optional.of(authheader);
        }

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header!=null || header.regionMatches(true,0,"Bearer",0 ,6)){
    String auth = authheader.substring(7);
            if(auth!=null && !auth.isBlank()){
                return Optional.of(auth);
            }
        }

        return Optional.empty();

    }

    @Override
    public Optional<?> readFromAccessToken(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if(header!=null&& header.regionMatches(true,0,"Bearer",0,6)){
            String authheader = header.substring(7);
            if(authheader!=null && !authheader.isBlank()){
                return Optional.of(authheader);
            }

        }
        return Optional.empty();
    }

}
