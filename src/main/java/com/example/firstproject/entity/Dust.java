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
public class Dust {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String sido_name;
    @Column
    private String station_name;
    @Column
    private String pm10Value;
    @Column
    private String pm10Grade;
    @Column
    private String pm25Value;
    @Column
    private String pm25Grade;

    public Dust(String sido_name, String station_name, String pm10Value, String pm10Grade, String pm25Value, String pm25Grade) {
        this.id = id;
        this.sido_name = sido_name;
        this.station_name = station_name;
        this.pm10Value = pm10Value;
        this.pm10Grade = pm10Grade;
        this.pm25Value = pm25Value;
        this.pm25Grade = pm25Grade;
    }
}
