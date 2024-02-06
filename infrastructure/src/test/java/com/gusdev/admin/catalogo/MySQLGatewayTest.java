package com.gusdev.admin.catalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE) //Digo para JVM quando e como utilizar as anotações
@Retention(RetentionPolicy.RUNTIME)
@Inherited //Caso alguém precise extender o comportamente dessa anotação
@ActiveProfiles("test") //Ativo o perfil de test para ele configurar o H2
//@SpringBootTest //Problema ao utilizar essa notação, o spring irá subir o contexto inteiro, absoluto; E esse não é nosso objetivo nesse momento, essa anotação é bom para testes integrados (end-to-end)
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")
}) //Digo para o Spring o que é para ele configurar além do que o #DataJpaTest configurou
//Esse tipo de filtro é um macete, vai ser através de REGEX, digo que quero buscar todas as classes que terminam com MySQLGateway
@DataJpaTest //Esse teste configura somente o necessário para testar o repositório //Problema de usar o DataJpaTest é que ele não enxerga o nosso gateway que está anotado com @service, porém ele é muito mais rápido
@ExtendWith(CleanUpExtension.class) //Extendo a classe 'CleanUpExtensions'
public @interface MySQLGatewayTest {

}
