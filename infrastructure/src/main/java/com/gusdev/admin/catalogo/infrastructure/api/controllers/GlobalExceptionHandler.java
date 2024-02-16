package com.gusdev.admin.catalogo.infrastructure.api.controllers;

import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import com.gusdev.admin.catalogo.domain.exceptions.NotFoundException;
import com.gusdev.admin.catalogo.domain.validation.Error;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

//Handler de erro Global (centralizar erros) --> iremos definir todos os handlers de Exception
//Facilita achar em que lugar está sendo tratado o erro 'x' e mudar a sua tratativa de acordo com suas necessidades
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = NotFoundException.class) //Digo para o spring que quando vier uma DomainException, é aqui que deverá cair, posso colocar vários exceptions para o mesmo handler
    public ResponseEntity<?> handleNotFoundException(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(ex)); //Como NotFound não possui payload, temos que construir um body, caso desejar.
    }

    @ExceptionHandler(value = DomainException.class) //Digo para o spring que quando vier uma DomainException, é aqui que deverá cair, posso colocar vários exceptions para o mesmo handler
    public ResponseEntity<?> handleDomainException(final DomainException ex) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    record ApiError(String message, List<Error> errors) {
        static ApiError from(final DomainException ex){
            return new ApiError(ex.getMessage(), ex.getErros());
        }
    }
}
