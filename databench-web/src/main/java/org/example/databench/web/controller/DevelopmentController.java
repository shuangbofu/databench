package org.example.databench.web.controller;

import org.example.databench.web.config.DaoContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by shuangbofu on 2021/9/11 1:42 下午
 */
@RestController("api/development")
public class DevelopmentController extends BaseController {

    public DevelopmentController(@Autowired DaoContext daoContext) {
        super(daoContext);
    }
}
