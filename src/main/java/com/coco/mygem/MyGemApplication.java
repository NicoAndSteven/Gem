package com.coco.mygem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.coco.mygem.mapper")
public class MyGemApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyGemApplication.class, args);
    }

}
