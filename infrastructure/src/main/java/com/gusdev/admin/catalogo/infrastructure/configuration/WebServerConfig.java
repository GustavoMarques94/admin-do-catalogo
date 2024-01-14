package com.gusdev.admin.catalogo.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//Notação para o spring entender que essa é uma classe de configuração, que ele vai ler e entender como bens gerenciáveis
@Configuration
//Notação onde digo o package padrão que ele vai precisar examinar que ele precisar para varrer clase por classe
    //procurando as classes que tem as anotações que ele utiliza para gerar um bean do spring
@ComponentScan("com.gusdev.admin.catalogo")
public class WebServerConfig {
}
