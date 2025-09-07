package com.patria.apps.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Schema(title = "Exception Response")
public class ExceptionResponse {

    private int status;
    private String message;
    private HttpStatus statusCode;
    
}
