package com.loctran.server.shared;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ResponseObject {
    private Date timestamp;
    private HttpStatus status;
    private Object data;
    private String message;
    private String path;
    
}
