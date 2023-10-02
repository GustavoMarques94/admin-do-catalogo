package com.gusdev.admin.catalogo.application.category.create;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.validation.handler.Notification;
import com.gusdev.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.Objects;

//Implementação concreta do caso de uso 'CreateCategoryUseCase'
public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    //Ex de injeção de dependência, onde a implementação do caso de uso depende de uma abstração 'CategoryGateway'
    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        //Garante que o 'categoryGateway' não seja nulo, se for, uma exceção 'NullPointerException' será lançada
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    //Either --> do inglês, ou isso ou aquilo, Notification -> Erro   CreateCategoryOutput --> Tudo correto
    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.isActive();

        final var notification = Notification.create();

        final var aCategory = Category.newCategory(aName, aDescription, isActive);
        //Valida a categoria, e se houver problemas de validação, uma exceção é lançada
        aCategory.validate(notification);


        //Crio uma nova categoria no 'categoryGateway' para criar a categoria no sistema
        //O resultado é passado para criar um objeto 'CreateCategoryOutput' que é então retornado pelo método
        /*return CreateCategoryOutput.from(this.categoryGateway.create(aCategory));*/
        return notification.hasError() ? API.Left(notification) : create(aCategory);
    }

    private Either<Notification, CreateCategoryOutput> create(Category aCategory) {
        return API.Try(() -> this.categoryGateway.create(aCategory)) //Try do vavr, mais expressivo
                .toEither() //converto o try em um either
                .bimap(Notification::create, CreateCategoryOutput::from); //converto o Throwable em um notification e
                    // converter um Category em um CreateCategoryOutput
    }


}
