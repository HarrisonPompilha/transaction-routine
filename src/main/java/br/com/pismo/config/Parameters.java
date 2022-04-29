package br.com.pismo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class Parameters {

    @Value("${application.version}")
    private String applicationVersion;
    
}
