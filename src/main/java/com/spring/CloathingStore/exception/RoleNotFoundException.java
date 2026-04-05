package com.spring.CloathingStore.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String message) {
        super(message);
    }
    RoleNotFoundException(){
        System.out.println("Role Not found");
    }
}
