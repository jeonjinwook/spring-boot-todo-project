package com.example.demo.todo.service;


import com.example.demo.todo.dto.RequestTodo;
import com.example.demo.todo.dto.ResponseTodo;
import com.example.demo.todo.repository.TodoRepository;
import com.example.demo.user.dto.ResponseDto;
import com.example.demo.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final ResponseDto responseDto;
    private final UserRepository userRepository;

    public ResponseEntity<?> getTodoList(RequestTodo todo) {

        List<ResponseTodo> result = new ArrayList<ResponseTodo>();

        try {
            if (todo.getSelectType().equals("ALL")) {
                result = todoRepository.findByTodoListFromUserId(todo.getUserNo());
            } else if (todo.getSelectType().equals("RECENTLY_TODO")) {
                result = todoRepository.findByRecentlyTodoFromUserId(todo.getUserNo());
            }


        } catch(Exception e) {

            e.printStackTrace();

        }
        return responseDto.success(result, "조회에 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> insertTodo(RequestTodo todo) {

        List<ResponseTodo> result = new ArrayList<ResponseTodo>();
        try {

            todoRepository.save(todo.toEntity());

            result = todoRepository.findByTodoListFromUserId(todo.getUserNo());

        } catch(Exception e) {

            e.printStackTrace();

        }
        return responseDto.success(result, "등록에 성공했습니다.",HttpStatus.OK);
    }

    public ResponseEntity<?> updateTodo(RequestTodo todo) {

        try {

            todoRepository.save(todo.toEntity());

        } catch(Exception e) {

            e.printStackTrace();

        }
        return responseDto.success(todo, "수정 성공했습니다.", HttpStatus.OK);
    }

    public ResponseEntity<?> deleteTodo(RequestTodo todo) {

        try {

            todoRepository.delete(todo.toEntity());

        } catch(Exception e) {

            e.printStackTrace();

        }
        return responseDto.success("삭제에 성공했습니다.");
    }

}
