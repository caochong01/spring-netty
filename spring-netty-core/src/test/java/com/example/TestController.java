package com.example;

import com.autumn.annotation.RouteMapping;
import com.autumn.annotation.PathVariable;
import com.autumn.mode.RequestMethod;
import com.autumn.annotation.NettyController;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;

@NettyController
@RouteMapping(value = "/testControl", method = {RequestMethod.GET, RequestMethod.POST})
public class TestController {

    @RouteMapping("/r0/:name")
    public int route0(HttpResponse response, Integer id, @PathVariable("name") String str, HttpRequest request) {

        return str.length();
    }

    @RouteMapping(value = "/r1", method = {RequestMethod.POST, RequestMethod.PUT})
    public String route1(Integer code) {
        return "11";
    }

    @RouteMapping("/r2/:name")
    public String route2(String name, TestBean testBean) {
        return "22";
    }

    @RouteMapping(value = "/r3/:id")
    public void route3(@PathVariable("id") Integer code) {
        System.out.println("我确实执行了，但我不返回" + code);
    }

}
