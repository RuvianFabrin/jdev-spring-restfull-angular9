package com.ru.jdevrestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = {"com.ru.jdevrestapi.model"})
@ComponentScan(basePackages = {"com.*"})
@EnableJpaRepositories(basePackages = {"com.ru.jdevrestapi.repository"})
@EnableTransactionManagement
@EnableWebMvc
@RestController
@EnableAutoConfiguration
@EnableCaching
public class JdevrestapiApplication implements WebMvcConfigurer {

    public static void main(String[] args) {

        SpringApplication.run(JdevrestapiApplication.class, args);
        //System.out.println(new BCryptPasswordEncoder().encode("123"));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/usuario/**")
                .allowedMethods("*")
                .allowedOrigins("*");

        registry.addMapping("/profissao/**")
                .allowedMethods("*")
                .allowedOrigins("*");

        registry.addMapping("/recuperar/**")
                .allowedMethods("*")
                .allowedOrigins("*");
    }
}
