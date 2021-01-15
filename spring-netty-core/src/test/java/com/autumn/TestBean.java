package com.autumn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Route("testRoute")
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
            System.out.println("hello, world!" + port);
            return "hello, world!";
        }
    }

}
