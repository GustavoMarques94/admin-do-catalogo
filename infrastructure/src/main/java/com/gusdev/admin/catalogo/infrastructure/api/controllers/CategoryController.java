package com.gusdev.admin.catalogo.infrastructure.api.controllers;

import com.gusdev.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import com.gusdev.admin.catalogo.domain.validation.handler.Notification;
import com.gusdev.admin.catalogo.infrastructure.api.CategoryAPI;
import com.gusdev.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(final CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand) //Precisamos tratar o retorno, pois ele é um Either, um retorno de sucesso e um retorno de falha
                .fold(onError, onSuccess); //Retorno de Falha --> LeftMapper //Retorno de sucesso --> RightMapper //o Fold recebe os dois
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String dir) {
        return null;
    }
}
