package com.spring.CloathingStore.exception;

public class UserNotEnable extends RuntimeException {
    public UserNotEnable(String message) {
        super(message);
    }
   UserNotEnable(){
       System.out.println("User is not Active");
   }
}
