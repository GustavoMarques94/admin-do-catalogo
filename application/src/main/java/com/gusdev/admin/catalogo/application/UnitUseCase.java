package com.gusdev.admin.catalogo.application;

//Caso de uso, Recebe alguma coisa e não retorna nada
public abstract class UnitUseCase<IN> {

    public abstract void execute(IN anIn);
}
