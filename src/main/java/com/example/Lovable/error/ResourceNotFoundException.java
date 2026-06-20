package com.example.Lovable.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{

    String resource;
    String resourceId;
}
