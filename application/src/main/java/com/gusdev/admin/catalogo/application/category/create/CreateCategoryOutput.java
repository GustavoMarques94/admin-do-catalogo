package com.gusdev.admin.catalogo.application.category.create;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryID;

public record CreateCategoryOutput(
        String id //O output não precisa ter todas as informações, apenas o ID da categoria recém criada
) {

    public static CreateCategoryOutput from(final String anId){
        return new CreateCategoryOutput(anId);
    }

    public static CreateCategoryOutput from(final Category aCategory){
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }
}
