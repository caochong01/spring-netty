package com.autumn;

import org.springframework.stereotype.Controller;

@Controller
@Route("/testControl")
public class TestController {

    @Route("/r0")
    public String route0(String str) {
        return "00";
    }

    @Route("/r1")
    public String route1(Integer code) {
        return "11";
    }

    @Route("/r2")
    public String route2() {
        return "22";
    }

}
