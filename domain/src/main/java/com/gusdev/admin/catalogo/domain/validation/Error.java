package com.gusdev.admin.catalogo.domain.validation;

//Classe de registro (record), utilizado para representar um conjunto imutável de dados e armazenar dados simples,
    //como mensagens de erro, sem a necessidade de escrever explicitamente os métodos getters, setters
public record Error(String message) {
}
