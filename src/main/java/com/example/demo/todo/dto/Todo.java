package com.example.demo.todo.dto;

import com.example.demo.user.dto.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@DynamicInsert
@DynamicUpdate
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "TODO_NO")
    private Long todoNo;

    @Column(name = "CONTENTS", nullable = false, length = 200)
    private String contents;

    @Column(name = "DATE", nullable = false, length = 10)
    private String date;

    @Column(name = "STATUS", nullable = false, length = 50)
    private String status;

    @Column(name = "USE_YN", length = 1)
    @ColumnDefault("'Y'")
    private String useYn;
    @CreationTimestamp
    private Date saveDttm;
    @ManyToOne(fetch = FetchType.LAZY)
    private Users user;


}

