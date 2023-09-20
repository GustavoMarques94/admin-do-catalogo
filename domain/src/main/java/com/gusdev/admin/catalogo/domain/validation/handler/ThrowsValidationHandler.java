package com.gusdev.admin.catalogo.domain.validation.handler;

import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import com.gusdev.admin.catalogo.domain.validation.Error;
import com.gusdev.admin.catalogo.domain.validation.ValidationHandler;

import java.util.List;

//Implementação da interface 'ValidationHandler'
public class ThrowsValidationHandler implements ValidationHandler {

    //Adiciona um erro representado pelo objeto 'Error' ao 'ValidationHandler'
    //Lança uma exceção 'DomainException' que encapsula o erro
    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(final ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public ValidationHandler validate(final Validation aValidation) {
        try {
            aValidation.validate();
        } catch (final Exception ex){
            throw DomainException.with(new Error(ex.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
