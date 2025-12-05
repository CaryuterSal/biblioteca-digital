package com.edu.utez.bibliotecadigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@ConfigurationPropertiesScan("com.edu.utez.bibliotecadigital.config")
@EnableWebSecurity
public class BibliotecaDigitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaDigitalApplication.class, args);
    }

}
