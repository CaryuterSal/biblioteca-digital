package com.edu.utez.bibliotecadigital.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "jwt")
@ConfigurationPropertiesScan
public record JwtProperties(String secret, Long expirationMs){
}
