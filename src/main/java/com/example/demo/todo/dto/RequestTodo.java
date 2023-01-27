package com.example.demo.todo.dto;

import com.example.cmn.UserCmnDto;
import com.example.demo.user.dto.Users;
import lombok.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestTodo extends UserCmnDto {

    private Long todoNo;        // Todo 번호
    private String contents;    // 내용
    private String status;      // 상태
    private String date;        // 날짜
    private String selectType;  // 조회 타입
    private Date saveDttm; // 저장 시간
    private String useYn; // 사용 여부
    public Todo toEntity() {
        return Todo.builder()
                .todoNo(todoNo)
                .contents(contents)
                .status(status)
                .date(date)
                .saveDttm(saveDttm)
                .useYn(useYn)
                .user(Users.builder().userNo(userNo).build())
                .build();
    }


}
