package com.autumn;

import org.springframework.stereotype.Controller;

@Controller
@RouteMapping("/testControl")
public class TestController {

    @RouteMapping("/r0")
    public String route0(String str) {
        return "00";
    }

    @RouteMapping("/r1")
    public String route1(Integer code) {
        return "11";
    }

    @RouteMapping("/r2")
    public String route2(String s, TestBean testBean) {
        return "22";
    }

}
