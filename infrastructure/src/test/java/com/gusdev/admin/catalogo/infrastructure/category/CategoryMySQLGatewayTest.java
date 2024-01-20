package com.gusdev.admin.catalogo.infrastructure.category;

import com.gusdev.admin.catalogo.infrastructure.MySQLGatewayTest;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//Classe que irá acoplar todos os testes do Gateway
@MySQLGatewayTest // All agregado que desejar fazer teste com o tipo MySQLGateway é só anotar com essa anotação que já estará configurado
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository; //Através do repositório, podemos manipular as informações no BD sem ter que passar toda hora pelo gateway;iremos fazer um mockap antes

    @Test
    public void testInjectedDpendencies(){
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(categoryRepository);
    }



}

