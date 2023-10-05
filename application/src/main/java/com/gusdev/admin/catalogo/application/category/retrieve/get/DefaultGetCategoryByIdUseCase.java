package com.gusdev.admin.catalogo.application.category.retrieve.get;

import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.category.CategoryID;
import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import com.gusdev.admin.catalogo.domain.validation.Error;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultGetCategoryByIdUseCase(final CategoryGateway categoryGateway){
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anIn) {
        final var anCategoryID = CategoryID.from(anIn);

        return this.categoryGateway.findById(anCategoryID) //vai até a camada de persistência do gateway, e busca por ID
                //se tiver um valor, o optinal estiver presente, ele vai no map, se não tiver nada ele vai no notFound
                //.map(aCategory -> CategoryOutput.from(aCategory)) //aCategory que recebemos do banco de dados
                .map(CategoryOutput::from) //Como o from é um método, podemos passar como referência
                .orElseThrow(notFound(anCategoryID));
    }

    private static Supplier<DomainException> notFound(CategoryID anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not-found".formatted(anId.getValue())));
    }
}
