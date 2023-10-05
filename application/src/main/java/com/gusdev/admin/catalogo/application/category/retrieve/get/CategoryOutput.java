package com.gusdev.admin.catalogo.application.category.retrieve.get;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryID;

import java.time.Instant;

//é o Resultado final do get, é a categoria em si
public record CategoryOutput(
    CategoryID id,
    String name,
    String description,
    boolean isActive,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {

    public static CategoryOutput from(final Category aCategory){
        return new CategoryOutput(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt()
        );
    }
}
