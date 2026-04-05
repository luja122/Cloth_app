package com.spring.CloathingStore.helper;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserHelper {
    public UUID praseUuid(String id){
        return UUID.fromString(id);
    }
}
