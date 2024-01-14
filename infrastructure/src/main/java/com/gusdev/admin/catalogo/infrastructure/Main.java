package com.gusdev.admin.catalogo.infrastructure;

import com.gusdev.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;

@SpringBootApplication //Notação fala pro Spring que ele tem que configurar algumas classes
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        //Seto uma variável de ambiente que o spring irá ler, e digo se não setar nenhuma propriedade ativa do spring, irei cair pro default, e a default é a development
        System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME,"development");
        //Seto já o spring como developmente já de cara, sem perfil padrão
        //System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME,"development");
        //Charmar o SpringApplication
        SpringApplication.run(WebServerConfig.class, args);
    }
}