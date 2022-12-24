package com.example.teamproject1.dto;

import com.example.teamproject1.entity.Data;
import lombok.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class DataForm {
    private Long id;
    private String name;
    private String start;
    private String enddate;
    private String kind;
    private int otherdate;
    private String nameinfo;
    private String image;
    private String othermonth;
    private String diffdays;
    public Data toEntity(){

        return new Data(id,name,start,enddate,kind,otherdate,nameinfo,image,othermonth,diffdays);
    }
}
