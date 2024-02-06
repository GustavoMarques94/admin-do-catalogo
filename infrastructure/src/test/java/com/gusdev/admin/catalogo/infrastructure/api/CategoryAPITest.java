package com.gusdev.admin.catalogo.infrastructure.api;

import com.gusdev.admin.catalogo.ControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    //Injetamos o MockMvc, tod o teste de cotroller, iremos utilizar o MockMvc
    //Ele irá nos auxiliar a configurar e fazer chamadas REST para os controllers que estamos passado na anotação lá em cima 'CategotyAPI.class'
    //Quem faz a configuração dele é o '@WebMvcTest'
    @Autowired
    private MockMvc mvc;

    @Test
    public void test(){

    }

}
