package com.gusdev.admin.catalogo.domain.validation;

//Classe base (pai) para outras classes de validadores
//Não será possível instanciar está classe, ele serve como um molde para classes derivadas que fornecerão
    //implementações específicas de validação
public abstract class Validator {

    //Próprio handler que vao utilizar para manipular
    //Validadores derivados usarão uma instância de 'ValidationHandler' para realizar as validações
    private final ValidationHandler handler;

    protected Validator(final ValidationHandler aHandler){
        this.handler = aHandler;
    }

    //Método abstrato que não tem uma implementação na classe base e deve ser implementado pelas subclasses
    //Cada subclasse de 'Validator' fornecerá sua própria lógica de validação ao implementar o método
    public abstract void validate();

    //Retorna a instância de 'ValidationHandler' associada ao validador
    //As subclasses podem usar esse método para acessar o manipulador de validação
    protected ValidationHandler validationHandler(){
        return this.handler;
    }

}
