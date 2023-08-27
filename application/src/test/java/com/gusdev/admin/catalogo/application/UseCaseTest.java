package com.gusdev.admin.catalogo.application;

import com.gusdev.admin.catalogo.domain.Category;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UseCaseTest {

    @Test //Notação necessário para que o Junit entenda que este é um método testável
    public void testCreateUseCase(){
        Assertions.assertNotNull(new UseCase());
        Assertions.assertNotNull(new UseCase().execute());
    }
}
