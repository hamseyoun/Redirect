package com.showcase.redirect.controller;


import org.joda.time.LocalDate;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.inject.Inject;

@Controller
@RequestMapping("/redirect")
public class RedirectController {

    private final ConversionService conversionService;  //스프링에서 제공하는 타입 컨버터


    @Inject
    public RedirectController(ConversionService conversionService) {
        this.conversionService = conversionService;
    }


    @GetMapping("/uriTemplate")
    public String uriTemplate(RedirectAttributes redirectAttrs) {
        //RedirectAttributes는 리다이렉트가 발생하기 직전에 모든 속성을 세션에 저장해준다.
        redirectAttrs.addAttribute("account", "a123");
        redirectAttrs.addAttribute("date", new LocalDate(2021, 02, 15));
        return "redirect:/redirect/{account}";
    }


    @GetMapping("/uriComponentsBuilder")
    public String uriComponentsBuilter() {
        String date = this.conversionService.convert(new LocalDate(2021, 12, 31), String.class);
        //"12/31/11"
        System.out.println("date = " + date);
        System.out.println("LocalDate = " + new LocalDate(2021, 12, 31).toString());
        System.out.println("==================");

        //UriComponents 클래스는 uri를 동적으로 생성해주는 클래스이다.
        //UriComponentsBuilder 클래스는 여러개의 파라미터들을 연결하여 uri로 만들어주는 기능을 한다.
        //UriComponentsBuilder는 queryParam()을 이용해서 파라미터를 연결하고,
        //expand()을 이용해서 {account} 템플릿변수에 값을 expand해준다.
        //이렇게 파라미터를 연결하고 템플릿변수에 넣은 String을 uri로 build()해주는것이다.
        UriComponents redirectUri = UriComponentsBuilder.fromPath("/redirect/{account}").queryParam("date", date)
                .build().expand("a123").encode();

        System.out.println("redirectUri = " + redirectUri.toUriString());   ///redirect/a123?date=12/31/21
        return "redirect:" + redirectUri.toUriString();
    }


    @GetMapping("/{account}")
    public String show(@PathVariable String account,
                       @RequestParam(required = false) LocalDate date) {
        System.out.println("account = " + account);
        return "redirect/redirectResults";
    }
}
