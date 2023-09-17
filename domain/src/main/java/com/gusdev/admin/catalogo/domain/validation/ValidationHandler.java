package com.gusdev.admin.catalogo.domain.validation;

import java.util.List;
import java.util.Objects;

//Interface que será uma ponte, para que cada vez que for validar, podermos implementar um handler diferente
//É um componente flexível para realizar validações, onde é possível definir diferentes manipuladores (handlers)
    //para as validações e implementar comportamentos específicos.
public interface ValidationHandler {

    //Método permite adicionar um erro, representado por um objeto 'Error', ao manipulador de validação
    //Isso permite que os manipuladores (Handlers) registrem erros encontrados durante o processo de validação
    ValidationHandler append(Error anError);

    //Método permite anexar outro manipulador de validação (Handler) a este manipulador
    //Pode ser útil quando desejar combinar vários manipuladores em uma única cadeia de validação
    ValidationHandler append(ValidationHandler anHandler);

    //Método utilizado para realizar uma validação específica, representado por um objeto 'Validation'
    //A implementação desse método deve contar a lógica de validação para a validação específica
    ValidationHandler validate(Validation aValidation);

    //Retorna uma lista de erros registrados no manipulador de validação (Handler)
    //Permite obter todas as msg de erro acumuladas durante o proceso de validação
    List<Error> getErrors();

    //Esse método irá validar se este ValidationHandler possui erro ou não dentro dele
    // True -> se houver erros no manipulador       False -> caso contrário
    default boolean hasError(){
        return !Objects.isNull(getErrors()) && !getErrors().isEmpty();
    }

    //INTERFACE ANINHADA chamada 'Validation' dentro da interace 'ValidationHandler'
    //Ela encapsula comportamentos específicos de validação
    //Permite que você passe validações personalizadas para o 'ValidationHandler'
        //o 'ValidationHandler' pode então executar essa validações personalidades definido no método validate
    public interface Validation{
        //Esse método deve ser implementado por qualquer classe que deseje realizar uma validação específica
        void validate();
    }
}
