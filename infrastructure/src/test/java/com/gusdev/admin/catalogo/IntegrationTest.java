package com.gusdev.admin.catalogo;

import com.gusdev.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target(ElementType.TYPE) //Digo para JVM quando e como utilizar as anotações
@Retention(RetentionPolicy.RUNTIME)
@Inherited //Caso alguém precise extender o comportamente dessa anotação
@ActiveProfiles("test") //Ativo o perfil de test para ele configurar o H2
@SpringBootTest(classes = WebServerConfig.class) //Classe mais global de testes; Iremos passar uma classe que ele irá usar para ler o metadados dela e entender quais configurações ele precisa habilitar no springboot
@ExtendWith(CleanUpExtension.class) //Extendo a classe 'CleanUpExtensions'
public @interface IntegrationTest {

}
