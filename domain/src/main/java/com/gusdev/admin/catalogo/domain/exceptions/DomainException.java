package com.gusdev.admin.catalogo.domain.exceptions;

import com.gusdev.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStacktraceException {

    private final List<Error> errors;
    private DomainException(final String aMessage, final List<Error> anErrors) {
        //Construtor da classe pai: RuntimeException
        super(aMessage);
        this.errors = anErrors;
    }

    //Factory Method
    public static DomainException with(final Error anErrors){
        return new DomainException(anErrors.message(),List.of(anErrors));
    }

    public static DomainException with(final List<Error> anErrors){
        return new DomainException("",anErrors);
    }

    public List<Error> getErros() {
        return errors;
    }
}
