package com.gusdev.admin.catalogo.infrastructure.api.controllers;

import com.gusdev.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.gusdev.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.gusdev.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.gusdev.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.gusdev.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.gusdev.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.gusdev.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import com.gusdev.admin.catalogo.domain.pagination.SearchQuery;
import com.gusdev.admin.catalogo.domain.validation.handler.Notification;
import com.gusdev.admin.catalogo.infrastructure.api.CategoryAPI;
import com.gusdev.admin.catalogo.infrastructure.category.models.CategoryResponse;
import com.gusdev.admin.catalogo.infrastructure.category.models.CreateCategoryRequest;
import com.gusdev.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.gusdev.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest input) {
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
        return this.listCategoriesUseCase
                .execute(new SearchQuery(page, perPage, search, sort, dir));
    }

    @Override
    public CategoryResponse getById(String id) {
        //Ambas as formas funcionam, olhar a explicação na interface 'CategoryApiPresenter'
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
        //return CategoryApiPresenter.present.apply(this.getCategoryByIdUseCase.execute(id));
        //return CategoryApiPresenter.present
        //        .compose(this.getCategoryByIdUseCase::execute) //1° chamo a função do compose
        //        .apply(id); //Com o resultado dessa função, chama ela aplicando o id (que é o parâmetro inicial)
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.ok(output);

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }
}
