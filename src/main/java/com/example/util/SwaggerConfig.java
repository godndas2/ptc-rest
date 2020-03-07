package com.example.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
@Component
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(getApiInfo());
    }

    private ApiInfo getApiInfo() {
        return new ApiInfo(
                "REST PTC backend Api Documentation",
                "This is REST API documentation of the Spring PTC backend. If authentication is enabled, when calling the APIs use admin/admin",
                "1.0",
                "PTC backend terms of service",
                new Contact(
                        "Vitaliy Fedoriv",
                        "https://github.com/spring-petclinic/spring-petclinic-rest",
                        "test@gmail.com"),
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0", Collections.emptyList());
    }
}
