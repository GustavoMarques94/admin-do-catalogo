package com.gusdev.admin.catalogo.infrastructure.category.presenters;

import com.gusdev.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.gusdev.admin.catalogo.infrastructure.category.models.CategoryResponse;

import java.util.function.Function;

public interface CategoryApiPresenter {

    //Ambos os métodos fazem a mesma coisa, são apenas formas diferentes de fazer um cast
    Function<CategoryOutput, CategoryResponse> present =
            output -> new CategoryResponse(
                    output.id().getValue(),
                    output.name(),
                    output.description(),
                    output.isActive(),
                    output.createdAt(),
                    output.updatedAt(),
                    output.deletedAt()
            );

    static CategoryResponse present(final CategoryOutput output){
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }
}
