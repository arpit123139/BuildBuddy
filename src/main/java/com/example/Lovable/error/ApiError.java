package com.example.Lovable.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ApiError {
    HttpStatus status;
    String message;
    LocalDateTime timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<ApiFieldError> errors;

    public ApiError(HttpStatus status,String msg){
        this.status=status;
        this.message=msg;
        this.timestamp=LocalDateTime.now();
    }

    public ApiError(HttpStatus status,String msg,List<ApiFieldError> errors){
        this.status=status;
        this.message=msg;
        this.timestamp=LocalDateTime.now();
        this.errors=errors;
    }
}
@Getter
class ApiFieldError{
    String field;
    String msd;

    public ApiFieldError(String field, String msd) {
        this.field = field;
        this.msd = msd;
    }
}