package com.spring.CloathingStore.controller;

import com.spring.CloathingStore.config.UserMapper;
import com.spring.CloathingStore.dtos.LoginRequest;
import com.spring.CloathingStore.dtos.LoginResponse;
import com.spring.CloathingStore.dtos.Userdto;
import com.spring.CloathingStore.exception.UserNotEnable;
import com.spring.CloathingStore.exception.UserNotFoundException;
import com.spring.CloathingStore.helper.UserHelper;
import com.spring.CloathingStore.model.RefreshToken;
import com.spring.CloathingStore.model.RefreshTokenRequest;
import com.spring.CloathingStore.model.Users;
import com.spring.CloathingStore.repo.RefreshtokenRepo;
import com.spring.CloathingStore.repo.UserRepo;
import com.spring.CloathingStore.security.JwtSecurity;
import com.spring.CloathingStore.service.AuthService;
import com.spring.CloathingStore.service.CookieService;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private CookieService cookieService;

    private final RefreshtokenRepo refreshtokenRepo;
    @Autowired
    private JwtSecurity jwtSecurity;
    @Autowired
    private UserHelper userHelper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private UserMapper userMapper;


    @PostMapping("/Register")
    public ResponseEntity<?> register(@RequestBody Userdto data){
      String response = authService.register(data);
      return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(HttpServletResponse response, @RequestBody LoginRequest data){
        LoginResponse loginResponse = authService.login(response,data);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/AccessToken")
    public ResponseEntity<String> refreshAccessToken(HttpServletRequest request){

        String accessToken = authService.readFromAccessToken(request)
                .orElseThrow(() -> new BadCredentialsException("Refresh token not found")).toString();

        if(!jwtSecurity.isRefreshToken(accessToken)){
            throw new BadCredentialsException("Invalid token");
        }

        UUID id = userHelper.praseUuid(jwtSecurity.getuserId(accessToken));

        Users user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if(!user.isEnable()){
            throw new UserNotEnable("User disabled");
        }

        if(jwtSecurity.isExpired(accessToken)){
            throw new BadCredentialsException("Refresh token expired");
        }

        String newAccessToken = jwtSecurity.generateAccessToken(user);

        return ResponseEntity.ok(newAccessToken);
    }
    @PostMapping("/refreshToken")
    public ResponseEntity<LoginResponse> refreshToken(HttpServletResponse response,
                                                     HttpServletRequest request,
                                                     @RequestBody(required = false) RefreshTokenRequest body

            ){
        String refreshtoken = authService.readFromToken(request,body).orElseThrow(()-> new BadCredentialsException("Refresh Token Not found")).toString();
        String jti = jwtSecurity.getJti(refreshtoken);
        UUID id =  userHelper.praseUuid(jwtSecurity.getuserId(refreshtoken));
        Users user = userRepo.findById(id).orElseThrow(()-> new UserNotFoundException("Cannot fetch user from the database"));
        RefreshToken refreshToken_data =  refreshtokenRepo.findByJti(jti).orElseThrow( ()-> new BadCredentialsException("Refresh token cannot Fetch the Data from The Database"));
        if(!jwtSecurity.isRefreshToken(refreshtoken)){
            throw new BadCredentialsException("id not a Refresh token");
        }
        if(refreshToken_data.isRevoked()){
            throw new RuntimeException("Refreshtoken is alreeady revoked");
        }
        if(!user.isEnable()){
            throw new UserNotEnable("User is not Enable");
        }
          if(refreshToken_data.getExpires_At().isBefore(Instant.now())){
              throw new BadCredentialsException("IS expired");
          }
          refreshToken_data.setRevoked(true);
          String newjti = UUID.randomUUID().toString();
          refreshToken_data.setReplacedBy(newjti);
          refreshtokenRepo.save(refreshToken_data);
          RefreshToken new_resfreshtoken = RefreshToken.builder()
                  .jti(newjti)
                  .id(UUID.randomUUID())
                  .created_At(Instant.now())
                  .expires_At(Instant.now().plusMillis(jwtSecurity.getRefresh_ttl()))
                  .user(user)
                  .build();
       String new_RefreshToken=   jwtSecurity.generateRefreshToken(user,newjti);
         String new_AccessToken=jwtSecurity.generateAccessToken(user);
      cookieService.attachRefreshtokenCookie(response,new_RefreshToken,(int) jwtSecurity.getRefresh_ttl());
      cookieService.addNoHeader(response);
      return ResponseEntity.ok(new LoginResponse(new_RefreshToken,new_AccessToken, "",jwtSecurity.getRefresh_ttl(),userMapper.user_to_UserDto(user)));
    }
    @DeleteMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
     Optional<?> refreshTokenobj= Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieService.getRefreshTokenCookieName()))
                .map(cookie -> cookie.getValue())
                .findFirst();
        if(refreshTokenobj.isPresent()){
            String refreshToken = refreshTokenobj.get().toString();
            String id = jwtSecurity.getuserId(refreshToken);
            UUID converted_id = userHelper.praseUuid(id);
            refreshtokenRepo.deleteById(converted_id);
            return ResponseEntity.ok("Logout Successfully");
        }else{
          return ResponseEntity.badRequest().body("Logout UnSuccessful");
        }
    }
}
