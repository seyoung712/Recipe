package com.example.teamproject1.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;


@Entity
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Getter
@Setter
public class Data {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String start;

    @Column
    private String enddate;

    @Column
    private String kind;

    @Column
    private int otherdate;

    //       private int otherdate이기에 get메소드로 불러온다.
    public int getOtherdate() {

//import java.util.Date 한다
//        Date객체를 불러온다.
        Date date = new Date();
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
// date1의 날짜 형식을 "yyyy-MM-dd" 로 정해준다.
        String date1 = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String date2 = getEnddate();
        LocalDate changeDate1 = LocalDate.parse(date1, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate changeDate2 = LocalDate.parse(date2, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//return 값으로는 changeDate1,changeDate2의 일수의 차이를 출력한다.
        return Period.between(changeDate1, changeDate2).getDays();
    }


    @Column
    private String nameinfo;

    @Column
    private String image;

    @Column
    private String othermonth;

    public int getOthermonth() {
        Date date = new Date();
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();
        String date1 = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));;
        String date2 = getEnddate();

        LocalDate changeDate1 = LocalDate.parse(date1, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate changeDate2 = LocalDate.parse(date2, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//changeDate1,changeDate2의 개월차이를 구한다.
        return Period.between(changeDate1, changeDate2).getMonths();
    }




    //getOthermonth, getOtherdate를 불러와서 남은일자를 if문에 따라서 처리한다.
    @Column
    private String diffdays;

    public String getDiffdays() {
        int month = getOthermonth();
        int date = getOtherdate();
        if (month<=0){
            if (date<0){
                return String.format("※폐기해야 합니다※");
            } else if (date==0) {
                return String.format("오늘까지 입니다");
            }
        }
        return String.format("%d개월 %d일 남음",month,date);
    }


}

