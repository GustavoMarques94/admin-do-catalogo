package com.gusdev.admin.catalogo.infrastructure.category.models;

import com.gusdev.admin.catalogo.JacksonTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

@JacksonTest //Notação que criamos para realizar testes de serialização
public class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json; //Propriedade que queremos testar


}
