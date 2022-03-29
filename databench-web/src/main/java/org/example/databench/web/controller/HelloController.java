package org.example.databench.web.controller;

import org.example.databench.web.annotations.ResultController;
import org.example.databench.web.config.DaoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by shuangbofu on 2021/9/9 11:58 上午
 */
@ResultController("/api")
public class HelloController extends BaseController {

    public HelloController(@Autowired DaoContext daoContext) {
        super(daoContext);
    }

    @RequestMapping("/hello")
    public Object hello() {
        return "hello";
    }
}
