package com.gusdev.admin.catalogo.domain.validation.handler;

import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import com.gusdev.admin.catalogo.domain.validation.Error;
import com.gusdev.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> erros) {
        this.errors = erros;
    }

    //Factory Method
    public static Notification create(){
        return new Notification(new ArrayList<>());
    }

    public static Notification create(final Error anError){
        return new Notification(new ArrayList<>()).append(anError);
    }

    @Override
    public Notification append(final Error anError) {
        this.errors.add(anError);

        return this;
    }

    @Override
    public Notification append(final ValidationHandler anHandler) {
        //Pego os erros desse validator e incluo todos no atual
        this.errors.addAll(anHandler.getErrors());
        return this;
    }

    @Override
    public Notification validate(final Validation aValidation) {
        try{
            aValidation.validate();
        } catch(final DomainException ex) {
            //Se vier um 'DomainException'
            this.errors.addAll(ex.getErros());
        } catch (final Throwable t){
            //Se vier um 'Throwable'
            this.errors.add(new Error(t.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }
}
