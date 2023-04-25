package com.example.meta.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Builder
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long member_id;
    @Column
    private String id;

    public Member(String sns_id, String nickname) {
        this.sns_id = sns_id;
        this.nickname = nickname;
    }

    @Column
    private String email;
    @Column
    private String password;

    @Column
    private String sns_id;
    @Column
    private String nickname;
    @Column
    private int role;
    @Column
    @CreationTimestamp
    private LocalDateTime create_time;
    @Column
    @UpdateTimestamp
    private LocalDateTime update_time;
}