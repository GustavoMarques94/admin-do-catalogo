package com.gusdev.admin.catalogo.application;

import com.gusdev.admin.catalogo.domain.category.Category;

//Estender a classe com um generics, nosso caso de uso por padrão vai entrar entrar alguma coisa e sair alguma coisa
//Nem sempre será assim!
public abstract class UseCase<IN, OUT> {

    //Modificador de aceeso
    //Por padrão, os casos de uso na literatura, eles implementam o pattern command, e o pattern command carrega
        //a semântica no nome da classe
    public abstract OUT execute(IN anIn);
}