package com.example.demo.todo.controller;

import com.example.demo.todo.dto.RequestTodo;
import com.example.demo.todo.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/todo")
@RestController
public class TodoController {

    private final TodoService todoService;

    @PostMapping("/getTodoList")
    public ResponseEntity<?> getTodoList(@RequestBody RequestTodo todo) {

        return todoService.getTodoList(todo);
    }

    @PostMapping("/insertTodo")
    public ResponseEntity<?> insertTodo(@RequestBody RequestTodo todo) {

        return todoService.insertTodo(todo);

    }
    @PutMapping("/updateTodo")
    public ResponseEntity<?> updateTodo(@RequestBody RequestTodo tode) {

        return todoService.updateTodo(tode);
    }

    @DeleteMapping("/deleteTodo")
    public ResponseEntity<?> deleteTodo(@RequestBody RequestTodo tode) {


        return todoService.deleteTodo(tode);
    }


}
