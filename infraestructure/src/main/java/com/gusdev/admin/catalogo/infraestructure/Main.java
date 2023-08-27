package com.gusdev.admin.catalogo.infraestructure;

import com.gusdev.admin.catalogo.application.UseCase;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        System.out.println(new UseCase().execute());
    }
}