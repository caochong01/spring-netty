package com.example;

import com.autumn.RouteMapping;
import com.autumn.SpringStaticEnv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

public class TestBean {

    @Service
    static class UserService {

        @Autowired
        private UserDao userDao;

        String getSysUser() {
            return userDao.getSysUser();
        }
    }

    @Controller
    @RouteMapping("testRoute")
    static class TestRoute {
        String getSysUser() {
            System.out.println("hello, world!");
            return "hello, world!";
        }
    }


    @Repository
    static class UserDao {

        @Value("${netty.config.port}")
        private String port;

        String getSysUser() {
            Environment environment = SpringStaticEnv.getENVIRONMENT();
            String yml = environment.getProperty("json.config.yml");
            System.out.println(yml);
            Boolean xml = environment.getProperty("json.config.xml", boolean.class);
            System.out.println(xml);
            System.out.println("hello, world!" + port);
            return "hello, world!";
        }
    }

}
