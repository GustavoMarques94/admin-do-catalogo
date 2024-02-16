package com.gusdev.admin.catalogo.domain;

//Classe que representa um identificador único para entidades
public abstract class Identifier extends ValueObject {

    public abstract String getValue(); // Método abstrato, quem implementa o Identifier precisa sobrescrever esse método
}
