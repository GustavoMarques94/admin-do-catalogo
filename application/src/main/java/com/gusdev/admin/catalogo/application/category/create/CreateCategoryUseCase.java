package com.gusdev.admin.catalogo.application.category.create;

import com.gusdev.admin.catalogo.application.UseCase;
import com.gusdev.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

//Fornece uma abstração (o que deve ser feito) para o caso de uso de criação de categorias. Ela herda a
    //estrutura genérica de um caso de uso (UseCase) e pode ser especializada por subclasses concretas para
    //implementar a lógica específica de criação de categorias.
//Não vai ser a implementação, será a abstração do caso de uso: 'CreateCategoryUseCase'
//Não trabalhe para uma implementação, trabalhe para uma abstração
//Alteração 1 -> Agora a saída é um Either de 'Notificatioin' ou 'CreateCategoryOutput'
public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

}
