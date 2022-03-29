package org.example.databench.starter;

import io.github.shuangbofu.helper.annotation.DaoScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Created by shuangbofu on 2021/9/9 11:59 上午
 */
@DaoScan(basePackages = "org.example.databench.persistence.dao",
        mapperBasePackages = "org.example.databench.persistence.dao.mapper")
@SpringBootApplication(scanBasePackages = {"org.example.databench"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
