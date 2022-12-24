package com.example.teamproject1.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;
import com.example.teamproject1.dto.DataForm;
import com.example.teamproject1.entity.Data;
import com.example.teamproject1.repository.DataRepository;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Period;

import javax.swing.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

import java.util.List;
import java.util.SimpleTimeZone;

@Controller
@Slf4j
public class Recipe {

    @Autowired
//데이터 저장 리파지터리
    private DataRepository dataRepository;
//메인페이지
    @GetMapping("/home")
    public String pagehome() {
        return "view/home";
    }

    //팀소개 이동(footer)
    @GetMapping("/info")
    public String pageinfo() {
        return "view/info";
    }

    //새로운 식자재 등록
    @PostMapping("/view/create")
    public String homecreate(DataForm form, Model model) {
        log.info(form.toString());

        //1.dto를 entity로 변환
        Data data = form.toEntity();
        log.info(data.toString());

        //2. entity를 repository에 저장.
        Data saved = dataRepository.save(data);
        log.info(saved.toString());


        log.info(data.toString());
        model.addAttribute("data", saved);
        //repository에 저장된 정보를 배열로 불러온다.
        List<Data> Datalist = dataRepository.findAll();

        //Datalist를 오름차순 정렬
        //Datalist를 내림차순 정렬하려면 Datalist.sort(Comparator.comparing(Data::getName).reversed()); 이다.
        Datalist.sort(Comparator.comparing(Data::getOtherdate));
        Datalist.sort(Comparator.comparing(Data::getOthermonth));

        log.info("Entity 입력 Data" + data.toString());

        model.addAttribute("datalist", Datalist);
        return "view/refrigerator";

    }
////수정



//냉장고


    //레시피 페이지에서 부류별로 정렬 클릭시 동작하는 컨트롤러
    @GetMapping("/textset")
    public String textset(Model model,RedirectAttributes rttr){
        List<Data> textset = dataRepository.findAll();
        model.addAttribute("datalist",textset);
        textset.sort(Comparator.comparing(Data::getKind));
        rttr.addFlashAttribute("textset", "부류별로 정렬하였습니다!");
        return "view/refrigerator";
    }
    //레시피 페이지에서 이름별로 정렬 클릭시 동작하는 컨트롤러
    @GetMapping("/nameset")
    public String dateset(Model model,RedirectAttributes rttr){
        List<Data> nameset = dataRepository.findAll();
        model.addAttribute("datalist",nameset);
        nameset.sort(Comparator.comparing(Data::getName));
        rttr.addFlashAttribute("nameset", "이름별로 정렬하였습니다!");

        return "view/refrigerator";
    }
    @GetMapping("/view/refrigerator")
    public String pagemypage(Model model) {
        List<Data> Datalist = dataRepository.findAll();
        model.addAttribute("datalist", Datalist);
        Datalist.sort(Comparator.comparing(Data::getOtherdate));
        Datalist.sort(Comparator.comparing(Data::getOthermonth));

        return "view/refrigerator";
    }
//게시판
    @GetMapping("/Community")
    public String pagelist() {
        return "view/Community";
    }

    @GetMapping("/Recipe")
    public String pagenew(DataForm form, Model model) {


        return "view/Recipe";
    }
//삭제 기능 구현
    @GetMapping("/listDelete/{id}")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes rttr, DataForm form) {

        log.info("삭제요청");
        //1. 대상을 가져온다
        Data target = dataRepository.findById(id).orElse(null);
        log.info("ddddd" + target.toString());
        //2. 대상 삭제한다.
        if (target != null) {
            dataRepository.delete(target);
            rttr.addFlashAttribute("deletemsg", target.getName()+"을(를) 삭제하였습니다!");
        }

        return "redirect:/view/refrigerator";
    }
//이미지 클릭시 식자재의 상세 정보 출력
    @GetMapping("imageshow/{id}")
    public String imageshow(@PathVariable Long id, Model model) {
        log.info("id="+id);

        Data dataEntity = dataRepository.findById(id).orElse(null);

        model.addAttribute("dataid", dataEntity);
        return "view/listshow";
    }
    @GetMapping("/recipelist1")
    //크롤링할 주소

    //크롤링 메소드
    public String goRegister(Model model) throws IOException {

        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6912734").get();
        //크롤링할 사이트를 불러온다
        Elements ingredient = doc.select("div[class=ready_ingre3]");

        Elements foodname = doc.select("div[class=view2_summary st3]").select("h3");
        Elements infoList1 = doc.select("div[class=view_step]").select("[id=stepdescr1]");
        Elements infoList2 = doc.select("div[class=view_step]").select("[id=stepdescr2]");
        Elements infoList3 = doc.select("div[class=view_step]").select("[id=stepdescr3]");
        Elements infoList4 = doc.select("div[class=view_step]").select("[id=stepdescr4]");
        Elements infoList5 = doc.select("div[class=view_step]").select("[id=stepdescr5]");

//
        //사이트에서 데이터 불러올 태그를 지정한다.
        System.out.println("foodname"+foodname);
        System.out.println("sdf"+infoList1.text());
//        System.out.println("sdf"+image1);
        //crawling 접속시 콘솔창에 데이터 출력된다.

        String Ingredient = ingredient.text();
        String Foodname = foodname.text();
        String infolist1 = infoList1.text();
        String infolist2 = infoList2.text();
        String infolist3 = infoList3.text();
        String infolist4 = infoList4.text();
        String infolist5 = infoList5.text();

        System.out.println("ingredient"+Ingredient);
        //태그에서 불러온 데이터를 텍스트만 추출한다.
        model.addAttribute("foodname",Foodname);
        model.addAttribute("infoList1",infolist1);
        model.addAttribute("infoList2",infolist2);
        model.addAttribute("infoList3",infolist3);
        model.addAttribute("infoList4",infolist4);
        model.addAttribute("infoList5",infolist5);
        model.addAttribute("Ingredient",Ingredient);
//        model.addAttribute("image1",image1);
        //infolist1을 모델로 묶어서 "view/register"에 출력한다.
        return "view/register1";
    }
    @GetMapping("/recipelist2")
    public String goRegister1(Model model) throws IOException{
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6992986").get();
        //크롤링할 사이트를 불러온다
        Elements ingredient = doc.select("div[class=ready_ingre3]");
        Elements foodname = doc.select("div[class=view2_summary st3]").select("h3");
        Elements infoList1 = doc.select("div[class=view_step]").select("[id=stepdescr1]");
        Elements infoList2 = doc.select("div[class=view_step]").select("[id=stepdescr2]");
        Elements infoList3 = doc.select("div[class=view_step]").select("[id=stepdescr3]");
        Elements infoList4 = doc.select("div[class=view_step]").select("[id=stepdescr4]");
        Elements infoList5 = doc.select("div[class=view_step]").select("[id=stepdescr5]");
        Elements infoList6 = doc.select("div[class=view_step]").select("[id=stepdescr6]");
        Elements infoList7 = doc.select("div[class=view_step]").select("[id=stepdescr7]");
        Elements infoList8 = doc.select("div[class=view_step]").select("[id=stepdescr8]");
        Elements infoList9 = doc.select("div[class=view_step]").select("[id=stepdescr9]");

//        Elements image1 = doc.select("div[id=stepimg1]");
        //사이트에서 데이터 불러올 태그를 지정한다.
        System.out.println("foodname"+foodname);
        System.out.println("sdf"+infoList1.text());
        System.out.println("sdf"+infoList2.text());
        System.out.println("sdf"+infoList3.text());
        System.out.println("sdf"+infoList4.text());
        System.out.println("sdf"+infoList5.text());
        System.out.println("sdf"+infoList6.text());
        System.out.println("sdf"+infoList7.text());
        System.out.println("sdf"+infoList8.text());
        System.out.println("sdf"+infoList9.text());

//        System.out.println("sdf"+image1);
        //crawling 접속시 콘솔창에 데이터 출력된다.
        String Ingredient = ingredient.text();
        String Foodname = foodname.text();
        String infolist1 = infoList1.text();
        String infolist2 = infoList2.text();
        String infolist3 = infoList3.text();
        String infolist4 = infoList4.text();
        String infolist5 = infoList5.text();
        String infolist6 = infoList6.text();
        String infolist7 = infoList7.text();
        String infolist8 = infoList8.text();
        String infolist9 = infoList9.text();

        //태그에서 불러온 데이터를 텍스트만 추출한다.
        model.addAttribute("foodname",Foodname);
        model.addAttribute("infoList1",infolist1);
        model.addAttribute("infoList2",infolist2);
        model.addAttribute("infoList3",infolist3);
        model.addAttribute("infoList4",infolist4);
        model.addAttribute("infoList5",infolist5);
        model.addAttribute("infoList6",infolist6);
        model.addAttribute("infoList7",infolist7);
        model.addAttribute("infoList8",infolist8);
        model.addAttribute("infoList9",infolist9);
        model.addAttribute("Ingredient",Ingredient);
//        model.addAttribute("image1",image1);
        //infolist1을 모델로 묶어서 "view/register"에 출력한다.
        return "view/register2";
    }
    @GetMapping("/recipelist3")
    public String goRegister2(Model model) throws IOException{
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6854292").get();
        Elements ingredient = doc.select("div[class=ready_ingre3]");
        Elements foodname = doc.select("div[class=view2_summary st3]").select("h3");
        Elements infoList1 = doc.select("div[class=view_step]").select("[id=stepdescr1]");
        Elements infoList2 = doc.select("div[class=view_step]").select("[id=stepdescr2]");
        Elements infoList3 = doc.select("div[class=view_step]").select("[id=stepdescr3]");
        Elements infoList4 = doc.select("div[class=view_step]").select("[id=stepdescr4]");
        Elements infoList5 = doc.select("div[class=view_step]").select("[id=stepdescr5]");
        Elements infoList6 = doc.select("div[class=view_step]").select("[id=stepdescr6]");
        Elements infoList7 = doc.select("div[class=view_step]").select("[id=stepdescr7]");

        String Ingredient = ingredient.text();
        String Foodname = foodname.text();
        String infolist1 = infoList1.text();
        String infolist2 = infoList2.text();
        String infolist3 = infoList3.text();
        String infolist4 = infoList4.text();
        String infolist5 = infoList5.text();
        String infolist6 = infoList6.text();
        String infolist7 = infoList7.text();

        model.addAttribute("foodname",Foodname);
        model.addAttribute("infoList1",infolist1);
        model.addAttribute("infoList2",infolist2);
        model.addAttribute("infoList3",infolist3);
        model.addAttribute("infoList4",infolist4);
        model.addAttribute("infoList5",infolist5);
        model.addAttribute("infoList6",infolist6);
        model.addAttribute("infoList7",infolist7);
        model.addAttribute("Ingredient",Ingredient);
        return "view/register3";
    }
    @GetMapping("/recipelist4")
    public String goRegister3(Model model) throws IOException{
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6989690").get();
        Elements ingredient = doc.select("div[class=ready_ingre3]");
        Elements foodname = doc.select("div[class=view2_summary st3]").select("h3");
        Elements infoList1 = doc.select("div[class=view_step]").select("[id=stepdescr1]");
        Elements infoList2 = doc.select("div[class=view_step]").select("[id=stepdescr2]");
        Elements infoList3 = doc.select("div[class=view_step]").select("[id=stepdescr3]");
        Elements infoList4 = doc.select("div[class=view_step]").select("[id=stepdescr4]");
        Elements infoList5 = doc.select("div[class=view_step]").select("[id=stepdescr5]");
        Elements infoList6 = doc.select("div[class=view_step]").select("[id=stepdescr6]");
        Elements infoList7 = doc.select("div[class=view_step]").select("[id=stepdescr7]");
        Elements infoList8 = doc.select("div[class=view_step]").select("[id=stepdescr8]");

        String Ingredient = ingredient.text();
        String Foodname = foodname.text();
        String infolist1 = infoList1.text();
        String infolist2 = infoList2.text();
        String infolist3 = infoList3.text();
        String infolist4 = infoList4.text();
        String infolist5 = infoList5.text();
        String infolist6 = infoList6.text();
        String infolist7 = infoList7.text();
        String infolist8 = infoList8.text();

        model.addAttribute("foodname",Foodname);
        model.addAttribute("infoList1",infolist1);
        model.addAttribute("infoList2",infolist2);
        model.addAttribute("infoList3",infolist3);
        model.addAttribute("infoList4",infolist4);
        model.addAttribute("infoList5",infolist5);
        model.addAttribute("infoList6",infolist6);
        model.addAttribute("infoList7",infolist7);
        model.addAttribute("infoList8",infolist8);
        model.addAttribute("Ingredient",Ingredient);
        return "view/register4";
    }
    @GetMapping("/recipelist5")
    public String goRegister4(Model model) throws IOException{
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6899874").get();
        Elements ingredient = doc.select("div[class=ready_ingre3]");
        Elements foodname = doc.select("div[class=view2_summary st3]").select("h3");
        Elements infoList1 = doc.select("div[class=view_step]").select("[id=stepdescr1]");
        Elements infoList2 = doc.select("div[class=view_step]").select("[id=stepdescr2]");
        Elements infoList3 = doc.select("div[class=view_step]").select("[id=stepdescr3]");
        Elements infoList4 = doc.select("div[class=view_step]").select("[id=stepdescr4]");
        Elements infoList5 = doc.select("div[class=view_step]").select("[id=stepdescr5]");
        Elements infoList6 = doc.select("div[class=view_step]").select("[id=stepdescr6]");
        Elements infoList7 = doc.select("div[class=view_step]").select("[id=stepdescr7]");
        Elements infoList8 = doc.select("div[class=view_step]").select("[id=stepdescr8]");
        Elements infoList9 = doc.select("div[class=view_step]").select("[id=stepdescr9]");

        String Ingredient = ingredient.text();
        String Foodname = foodname.text();
        String infolist1 = infoList1.text();
        String infolist2 = infoList2.text();
        String infolist3 = infoList3.text();
        String infolist4 = infoList4.text();
        String infolist5 = infoList5.text();
        String infolist6 = infoList6.text();
        String infolist7 = infoList7.text();
        String infolist8 = infoList8.text();
        String infolist9 = infoList9.text();

        model.addAttribute("foodname",Foodname);
        model.addAttribute("infoList1",infolist1);
        model.addAttribute("infoList2",infolist2);
        model.addAttribute("infoList3",infolist3);
        model.addAttribute("infoList4",infolist4);
        model.addAttribute("infoList5",infolist5);
        model.addAttribute("infoList6",infolist6);
        model.addAttribute("infoList7",infolist7);
        model.addAttribute("infoList8",infolist8);
        model.addAttribute("infoList9",infolist9);
        model.addAttribute("Ingredient",Ingredient);
        return "view/register5";
    }
}
