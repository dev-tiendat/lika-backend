package com.app.lika.exception;

import com.app.lika.payload.response.APIResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.ResponseEntity;

@EqualsAndHashCode(callSuper = true)
@Data
public class ResponseEntityErrorException extends RuntimeException {
    private static final long serialVersionUID = -3156815846745801694L;

    private transient ResponseEntity<APIResponse> apiResponse;

    public ResponseEntityErrorException(ResponseEntity<APIResponse> apiResponse) {
        this.apiResponse = apiResponse;
    }
}
