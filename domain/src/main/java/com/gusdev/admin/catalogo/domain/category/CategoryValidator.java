package com.gusdev.admin.catalogo.domain.category;

import com.gusdev.admin.catalogo.domain.validation.Error;
import com.gusdev.admin.catalogo.domain.validation.ValidationHandler;
import com.gusdev.admin.catalogo.domain.validation.Validator;

import java.util.Objects;

public class CategoryValidator extends Validator {

    //Categoria que quero validar
    private final Category category;

    //Handler que vai saber lidar com os erros
    public CategoryValidator(final Category aCategory, final ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        if (Objects.isNull(this.category.getName())){
            this.validationHandler().append(new Error("'name' should not be null"));
        }
    }
}
