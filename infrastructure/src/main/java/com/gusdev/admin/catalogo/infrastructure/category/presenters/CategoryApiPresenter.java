package com.gusdev.admin.catalogo.infrastructure.category.presenters;

import com.gusdev.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.gusdev.admin.catalogo.infrastructure.category.models.CategoryApiOutput;

import java.util.function.Function;

public interface CategoryApiPresenter {

    //Ambos os métodos fazem a mesma coisa, são apenas formas diferentes de fazer um cast
    Function<CategoryOutput, CategoryApiOutput> present =
            output -> new CategoryApiOutput(
                    output.id().getValue(),
                    output.name(),
                    output.description(),
                    output.isActive(),
                    output.createdAt(),
                    output.updatedAt(),
                    output.deletedAt()
            );

    static CategoryApiOutput present(final CategoryOutput output){
        return new CategoryApiOutput(
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
