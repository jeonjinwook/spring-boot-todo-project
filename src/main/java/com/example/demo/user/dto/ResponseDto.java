package com.example.demo.user.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ResponseDto {

    @Getter
    @Builder
    private static class Body {
        private int state;
        private String result;
        private String message;
        private Object data;
        private Object error;
    }

    public ResponseEntity<?> success(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
            .state(status.value())
            .data(data)
            .result("success")
            .message(msg)
            .error(Collections.emptyList())
            .build();
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> success(String msg) {
        return success(Collections.emptyList(), msg, HttpStatus.OK);
    }

    public ResponseEntity<?> fail(Object data, String msg, HttpStatus status) {
        Body body = Body.builder()
            .state(status.value())
            .data(data)
            .result("fail")
            .message(msg)
            .error(Collections.emptyList())
            .build();
        return ResponseEntity.ok(body);
    }

    public ResponseEntity<?> fail(String msg, HttpStatus status) {
        return fail(Collections.emptyList(), msg, status);
    }

}
