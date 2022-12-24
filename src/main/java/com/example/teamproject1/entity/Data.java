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


    public int getOtherdate() {
        Date date = new Date();
        LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();

        String date1 = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String date2 = getEnddate();

        LocalDate changeDate1 = LocalDate.parse(date1, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate changeDate2 = LocalDate.parse(date2, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return Period.between(changeDate1, changeDate2).getDays();
    }


    @Column
    private String nameinfo;

    @Column
    private String image;

    @Column
    private String othermonth;

    public int getOthermonth() {

        String date1 = getStart();
        String date2 = getEnddate();

        LocalDate changeDate1 = LocalDate.parse(date1, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate changeDate2 = LocalDate.parse(date2, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return Period.between(changeDate1, changeDate2).getMonths();
    }

    @Column
    private String plusdate;


    //getOthermonth, getOtherdate를 불러와서 남은일자를 if문에 따라서 처리한다.
    @Column
    private String diffdays;

    public String getDiffdays() {
        int month = getOthermonth();
        int date = getOtherdate();
        if (month==0){
            if (date<0){
                return String.format("※폐기해야 합니다※");
            } else if (date==0) {
                return String.format("오늘까지 입니다");
            }
        }
        return String.format("%d개월 %d남음",month,date);
    }//getDiffdays()


} //class Data

