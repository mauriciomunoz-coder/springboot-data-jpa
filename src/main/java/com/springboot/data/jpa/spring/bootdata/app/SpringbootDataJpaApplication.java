package com.springboot.data.jpa.spring.bootdata.app;

import com.springboot.data.jpa.spring.bootdata.app.models.service.IUploadFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringbootDataJpaApplication implements CommandLineRunner {
    @Autowired
    IUploadFileService iuploadFileService;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDataJpaApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        iuploadFileService.deleteAll();
        iuploadFileService.init();
    }
}
