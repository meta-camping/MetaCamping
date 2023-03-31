package com.example.firstproject.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @CreatedDate
    private String CreatedTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

    @LastModifiedDate
    private String UpdatedTime = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

}
