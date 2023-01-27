package com.example.demo.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseTodo {

    private Long todoNo;        // Todo 번호
    private String contents;    // 내용
    private String status;      // 상태
    private String date;        // 날짜
    private Date saveDttm; // 저장 시간
    private String useYn; // 사용 여부

}
