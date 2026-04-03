package com.spring.CloathingStore.service;

import com.spring.CloathingStore.model.Users;
import com.spring.CloathingStore.repo.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    private UserRepo userRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("User Email not found"));
       return new UserPrinciple(user);

    }
}
