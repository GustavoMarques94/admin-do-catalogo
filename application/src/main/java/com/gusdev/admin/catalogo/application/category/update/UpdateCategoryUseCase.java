package com.gusdev.admin.catalogo.application.category.update;

import com.gusdev.admin.catalogo.application.UseCase;
import com.gusdev.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification,UpdateCategoryOutput>> {
    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand anIn) {
        return null;
    }
}
