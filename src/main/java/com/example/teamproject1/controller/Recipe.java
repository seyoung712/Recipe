package com.example.teamproject1.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import com.fasterxml.jackson.databind.util.JSONPObject;

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
//       Datalist들의 값들을 view/refrigerator 페이지에 출력한다.
        return "view/refrigerator";

    }

//냉장고

    //레시피 페이지에서 부류별로 정렬 클릭시 동작하는 컨트롤러
    @GetMapping("/textset")
    public String textset(Model model,RedirectAttributes rttr){
//      리파지터리에서 데이터를 모두 찾는다
        List<Data> textset = dataRepository.findAll();
//        모델을 사용해서 textset배열을 datalist로 지정한다.
        model.addAttribute("datalist",textset);
//        데이터 정렬 순서를 getKind 순서에 맞게 나열한다.
        textset.sort(Comparator.comparing(Data::getKind));
//        버튼 클릭시 알림메시지를 출력한다.
        rttr.addFlashAttribute("textset", "부류별로 정렬하였습니다!");
        return "view/refrigerator";
    }
    //레시피 페이지에서 이름별로 정렬 클릭시 동작하는 컨트롤러
    @GetMapping("/nameset")
    public String dateset(Model model,RedirectAttributes rttr){
//        리파지터리에 있는 데이터를 모두 찾는다.
        List<Data> nameset = dataRepository.findAll();
//        모델을 사용해서 nameset을 datalist로 지정한다.
        model.addAttribute("datalist",nameset);
//        데이터 정열을 getName에 맞게 나열한다.
        nameset.sort(Comparator.comparing(Data::getName));
//        버튼 클릭시 알림메시지를 출력한다.
        rttr.addFlashAttribute("nameset", "이름별로 정렬하였습니다!");
//        데이터 값들을 return 값에 표시한다.
        return "view/refrigerator";
    }
//    /view/refrigerator 호출시 동작되는 컨트롤러
    @GetMapping("/view/refrigerator")

    public String pagemypage(Model model) {
//        리파지터리에 저장된 데이터를 모두 찾는다
        List<Data> Datalist = dataRepository.findAll();
//        Datalist를 datalist로 지정한다.
        model.addAttribute("datalist", Datalist);
//        정렬을 getOtherdate,getOthermonth순서로 나열한다.
        Datalist.sort(Comparator.comparing(Data::getOtherdate));
        Datalist.sort(Comparator.comparing(Data::getOthermonth));

        return "view/refrigerator";
    }
//게시판
//    @GetMapping("/Community")
//    public String pagelist() {
//        return "view/Community";
//    }

//    Recipe 호출시 리턴값에있는 페이지로 표시된다
    @GetMapping("/Recipe")
    public String pagenew(DataForm form, Model model) {


        return "view/Recipe";
    }
//삭제 기능 구현
    @GetMapping("/listDelete/{id}")
    public String delete(@PathVariable Long id, Model model, RedirectAttributes rttr, DataForm form) {
//      /listDelete/{id} 지정된 id 값을 삭제했을시 발생하는 log이다. 콘솔창에 출력된다.
        log.info("삭제요청");
        //1. 대상을 가져온다
        Data target = dataRepository.findById(id).orElse(null);
        log.info("ddddd" + target.toString());
        //2. 대상 삭제한다.
        if (target != null) {
//            target이 null이 아니면 리파지터리에서 target을 delete한다.
            dataRepository.delete(target);
//            지정된 target 즉 id 값이 삭제되었을경우에 발생되는 알림메시지이다.
            rttr.addFlashAttribute("deletemsg", target.getName()+"을(를) 삭제하였습니다!");
        }

        return "redirect:/view/refrigerator";
    }
//이미지 클릭시 식자재의 상세 정보 출력
    @GetMapping("imageshow/{id}")
    public String imageshow(@PathVariable Long id, Model model) {
        log.info("id="+id);
//      호출시 콘솔창에 출력된다.
        Data dataEntity = dataRepository.findById(id).orElse(null);
// 리파지터리에서 id값과 id값이거나 null값을 찾아서 dataEntity에 저장한다.
        model.addAttribute("dataid", dataEntity);
        return "view/listshow";
    }
    @GetMapping("/recipelist1")
    //크롤링할 주소

    //크롤링 메소드
    public String goRegister(Model model) throws IOException {
//크롤링할 사이트를 불러온다
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6912734").get();
//        크롤링한 페이지에서 추출할 html 태그 코드를 찾아서 입력한다.
        //        재료들을 크롤링한다
        Elements ingredient = doc.select("div[class=ready_ingre3]");
//        음식이름을 크롤링한다.
        Elements foodname = doc.select("div[class=view2_summary st3]").select("h3");
//         만드는 방법과, 순서를 출력한다.
        Elements infoList1 = doc.select("div[class=view_step]").select("[id=stepdescr1]");
        Elements infoList2 = doc.select("div[class=view_step]").select("[id=stepdescr2]");
        Elements infoList3 = doc.select("div[class=view_step]").select("[id=stepdescr3]");
        Elements infoList4 = doc.select("div[class=view_step]").select("[id=stepdescr4]");
        Elements infoList5 = doc.select("div[class=view_step]").select("[id=stepdescr5]");
//        foodname 과 infoList가 크롤링되는지 콘솔창에서 확인한다.
//        crawling 접속시 콘솔창에 데이터 출력된다.
        System.out.println("foodname"+foodname);
        System.out.println("sdf"+infoList1.text());

//      태그된 코드를 그냥 출력할시에 html 코드가 전부 출력된다.
//      따라서 뒤에 text()를 붙여서 text만 가져와서 출력하도록 만든다.
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
        //infolist1을 모델로 묶어서 "view/register"에 출력한다.
        return "view/register1";
    }
    @GetMapping("/recipelist2")
    public String goRegister1(Model model) throws IOException{
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6992986").get();
        //크롤링할 사이트를 불러온다
//        크롤링한 페이지에서 추출할 html 태그 코드를 찾아서 입력한다.
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

        //recipelist2 접속시 콘솔창에 데이터 출력된다.
        System.out.println("foodname"+foodname);
        System.out.println(infoList1.text());
        System.out.println(infoList2.text());
        System.out.println(infoList3.text());
        System.out.println(infoList4.text());
        System.out.println(infoList5.text());
        System.out.println(infoList6.text());
        System.out.println(infoList7.text());
        System.out.println(infoList8.text());
        System.out.println(infoList9.text());


        //태그에서 불러온 데이터를 텍스트만 추출한다.
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

        //infolist 들을 모델로 묶어서 "view/register"에 출력한다.
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

    @GetMapping("/recipelist6")
    public String goRegister5(Model model) throws IOException {
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6897374").get();
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
        return "view/register6";
    }
    @GetMapping("/recipelist7")
    public String goRegister6(Model model) throws IOException {
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6894872").get();
        Elements ingredient = doc.select("div[class=ready_ingre3]");
        Elements foodname = doc.select("div[class=view2_summary st3]").select("h3");
        Elements infoList1 = doc.select("div[class=view_step]").select("[id=stepdescr1]");
        Elements infoList2 = doc.select("div[class=view_step]").select("[id=stepdescr2]");
        Elements infoList3 = doc.select("div[class=view_step]").select("[id=stepdescr3]");
        Elements infoList4 = doc.select("div[class=view_step]").select("[id=stepdescr4]");
        Elements infoList5 = doc.select("div[class=view_step]").select("[id=stepdescr5]");
        Elements infoList6 = doc.select("div[class=view_step]").select("[id=stepdescr6]");



        String Ingredient = ingredient.text();
        String Foodname = foodname.text();
        String infolist1 = infoList1.text();
        String infolist2 = infoList2.text();
        String infolist3 = infoList3.text();
        String infolist4 = infoList4.text();
        String infolist5 = infoList5.text();
        String infolist6 = infoList6.text();



        model.addAttribute("foodname",Foodname);
        model.addAttribute("infoList1",infolist1);
        model.addAttribute("infoList2",infolist2);
        model.addAttribute("infoList3",infolist3);
        model.addAttribute("infoList4",infolist4);
        model.addAttribute("infoList5",infolist5);
        model.addAttribute("infoList6",infolist6);
        model.addAttribute("Ingredient",Ingredient);
        return "view/register7";
    }
    @GetMapping("/recipelist8")
    public String goRegister7(Model model) throws IOException {
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/4197710").get();
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
        return "view/register8";
    }
    @GetMapping("/recipelist9")
    public String goRegister8(Model model) throws IOException {
        Document doc = Jsoup.connect("https://www.10000recipe.com/recipe/6890777").get();
        Elements ingredient = doc.select("div[class=ready_ingre3]");
        Elements foodname = doc.select("div[class=view2_summary st3]").select("h3");
        Elements infoList1 = doc.select("div[class=view_step]").select("[id=stepdescr1]");
        Elements infoList2 = doc.select("div[class=view_step]").select("[id=stepdescr2]");
        Elements infoList3 = doc.select("div[class=view_step]").select("[id=stepdescr3]");
        Elements infoList4 = doc.select("div[class=view_step]").select("[id=stepdescr4]");
        Elements infoList5 = doc.select("div[class=view_step]").select("[id=stepdescr5]");




        String Ingredient = ingredient.text();
        String Foodname = foodname.text();
        String infolist1 = infoList1.text();
        String infolist2 = infoList2.text();
        String infolist3 = infoList3.text();
        String infolist4 = infoList4.text();
        String infolist5 = infoList5.text();




        model.addAttribute("foodname",Foodname);
        model.addAttribute("infoList1",infolist1);
        model.addAttribute("infoList2",infolist2);
        model.addAttribute("infoList3",infolist3);
        model.addAttribute("infoList4",infolist4);
        model.addAttribute("infoList5",infolist5);

        model.addAttribute("Ingredient",Ingredient);
        return "view/register9";
    }
    }
//    @GetMapping("/api")
//    public String callApi(Model model) throws IOException{
//        StringBuilder result = new StringBuilder();
//
//        String urlStr = "http://openapi.foodsafetykorea.go.kr/api/836143f96d9c4e5db4f7/COOKRCP01/xml/1/5";
//
//        URL url = new URL(urlStr);
//
//        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//        urlConnection.setRequestMethod("GET");
//
//        BufferedReader br;
//
//        br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));
//
//        String returnLine;
//
//        while ((returnLine = br.readLine())!=null){
//            result.append(returnLine+"\n\r");
//        }
//        urlConnection.disconnect();
//
//        model.addAttribute("result",result);
//        log.info(result.toString());
//
//
//
//        return "view/api";
//    }




