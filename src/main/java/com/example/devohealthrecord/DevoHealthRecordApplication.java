package com.example.devohealthrecord;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@SpringBootApplication
//@EnableSwagger2
public class DevoHealthRecordApplication {

    public static void main(String[] args) {
        SpringApplication.run(DevoHealthRecordApplication.class, args);
    }

    @Bean
    public Docket swaggerConfiguration(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/*"))
                .build()
                .apiInfo(apiDetails());
    }

    private ApiInfo apiDetails() {

        return new ApiInfo(

                "Health Recording management System",

                "Management of health tracking data",

                "1.0",

                "Free to use",

                new springfox.documentation.service.Contact("Koushik Kothagal", "http://devodata.io", "alb.com"),

                "API License",

                "http://devodata.io",

                Collections.emptyList());
    }

}
