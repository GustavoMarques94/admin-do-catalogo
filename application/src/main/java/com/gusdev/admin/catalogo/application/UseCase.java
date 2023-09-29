package com.gusdev.admin.catalogo.application;

//Estrutura genérica para definir casos de uso em um sistema.
//Cada caso de uso específico deve herdar dessa classe e implementar o método execute conforme as necessidades
    //do caso de uso
//A flexibilidade dos tipos genéricos permite que você crie casos de uso para uma variedade de entradas e saídas,
    //mantendo a estrutura comum fornecida pela classe abstrata genérica.
//IN e OUT são tipos genéricos que serão fornecidos ao criar uma instância de classe
//Ex: você pode criar um caso de uso que recebe uma String como entrada (IN) e retorna um Integer como saída (OUT)
public abstract class UseCase<IN, OUT> {

    //Método Abstrato, qualquer classe que herde de 'UseCase' precisa implementar esse método
    //Por padrão, os casos de uso na literatura, eles implementam o pattern command, e o pattern command carrega
        //a semântica no nome da classe
    public abstract OUT execute(IN anIn);
}