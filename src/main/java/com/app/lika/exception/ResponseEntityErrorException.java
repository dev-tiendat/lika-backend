package com.app.lika.exception;

import com.app.lika.payload.response.APIMessageResponse;
import org.springframework.http.ResponseEntity;

public class ResponseEntityErrorException extends RuntimeException {
    private static final long serialVersionUID = -3156815846745801694L;

    private transient ResponseEntity<APIMessageResponse> apiMessageResponse;

    public ResponseEntityErrorException(ResponseEntity<APIMessageResponse> apiMessageResponse) {
        this.apiMessageResponse = apiMessageResponse;
    }

    public ResponseEntity<APIMessageResponse> getApiMessageResponse() {
        return apiMessageResponse;
    }
}
