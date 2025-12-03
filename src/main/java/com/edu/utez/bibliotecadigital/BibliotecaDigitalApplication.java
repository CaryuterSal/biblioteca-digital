package com.edu.utez.bibliotecadigital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan("com.edu.utez.bibliotecadigital.config")
public class BibliotecaDigitalApplication {

    public static void main(String[] args) {
        SpringApplication.run(BibliotecaDigitalApplication.class, args);
    }

}
