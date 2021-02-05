package com.example;

import com.autumn.mode.RequestMethod;
import com.autumn.router.Router;

public class TestRouter {

    public static void main(String[] args) {
        Router router = new Router();
        router.pattern(RequestMethod.GET, "/test/tt/:id", TestBean.UserService.class, "");
        router.route("/test/123");
    }
}
