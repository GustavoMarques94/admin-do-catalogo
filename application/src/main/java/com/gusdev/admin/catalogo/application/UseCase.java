package com.gusdev.admin.catalogo.application;

import com.gusdev.admin.catalogo.domain.category.Category;

public class UseCase {
    public Category execute(){
        return Category.newCategory("Teste","Teste1",true);
    }
}