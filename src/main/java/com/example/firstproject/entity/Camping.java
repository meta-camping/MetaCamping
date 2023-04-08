package com.example.firstproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity // DB가 해당 객체를 인식 가능!
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter // 롬복으로 게터 추가
public class Camping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int num;
    @Column
    private String address;
    @Column
    private String road_address;
    @Column
    private String name;
    @Column
    private String wgs84_x;
    @Column
    private String wgs84_y;
    @Column
    private String city_name;
}
