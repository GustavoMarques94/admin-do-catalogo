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
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        final var name = this.category.getName();
        if (Objects.isNull(name)){
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()){
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if (length > 255 || length < 3){
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characteres"));
        }
    }
}
