package com.gusdev.admin.catalogo.domain.exceptions;

import com.gusdev.admin.catalogo.domain.AggregateRoot;
import com.gusdev.admin.catalogo.domain.Identifier;
import com.gusdev.admin.catalogo.domain.validation.Error;

import java.util.Collections;
import java.util.List;

//Vamos definir os atributos da nossa Exceção
public class NotFoundException extends DomainException {

    protected NotFoundException(final String aMessage, final List<Error> anErrors) {
        super(aMessage, anErrors);
    }

    public static NotFoundException  with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id
            ){

        final var anError = "%s with ID %s was not found".formatted(
                anAggregate.getSimpleName(), //Nome simples da classe, nome que declaramos
                id.getValue()
        );

        return new NotFoundException(anError, Collections.emptyList());
    }
}
