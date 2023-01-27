package com.example.demo.todo.repository;

import com.example.demo.todo.dto.ResponseTodo;
import com.example.demo.todo.dto.Todo;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long>, JpaSpecificationExecutor<Todo> {

    @Query(value ="SELECT NEW com.example.demo.todo.dto.ResponseTodo(t.id, t.contents, t.status, t.date, t.saveDttm, t.useYn) " +
                    "FROM Todo t where t.user.id = :userNo " +
                "ORDER BY t.saveDttm DESC")
    public List<ResponseTodo> findByTodoListFromUserId(@Param("userNo") long userNo);

    @Query(value ="SELECT NEW com.example.demo.todo.dto.ResponseTodo(t.id, t.contents, t.status, t.date, t.saveDttm, t.useYn) " +
                    "FROM Todo t " +
                   "WHERE t.user.id = :userNo " +
                     "AND t.saveDttm = (SELECT MAX(saveDttm) FROM Todo WHERE user.id = :userNo) " +
                   "ORDER BY t.saveDttm DESC ")
    public List<ResponseTodo> findByRecentlyTodoFromUserId(@Param("userNo") long userNo);

}
