package com.example;

import com.autumn.RouteMapping;
import com.autumn.mode.RequestMethod;
import com.autumn.NettyController;

@NettyController
@RouteMapping(value = "/testControl", method = {RequestMethod.GET, RequestMethod.POST})
public class TestController {

    @RouteMapping("/r0")
    public String route0(String str) {
        return "00";
    }

    @RouteMapping(value = "//:id/r1", method = {RequestMethod.POST, RequestMethod.PUT})
    public String route1(Integer code) {
        return "11";
    }

    @RouteMapping("/r2///:name")
    public String route2(String s, TestBean testBean) {
        return "22";
    }

}
