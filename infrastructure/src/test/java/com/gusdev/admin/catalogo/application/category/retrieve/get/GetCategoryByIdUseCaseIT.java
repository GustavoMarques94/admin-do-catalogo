package com.gusdev.admin.catalogo.application.category.retrieve.get;

import com.gusdev.admin.catalogo.IntegrationTest;
import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.category.CategoryID;
import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import com.gusdev.admin.catalogo.domain.exceptions.NotFoundException;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;


    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId();

        save(aCategory);

        final var actualCategory = useCase.execute(expectedId.getValue());

        //Como estava com uma pequena diferença nos nanosegundos da criação de uma nova categoria, trunquei algumas casas decimais para o teste passar
        ZonedDateTime expectedCreatedAt = ZonedDateTime.parse(aCategory.getCreatedAt().toString());
        ZonedDateTime actualCreatedAt = ZonedDateTime.parse(actualCategory.createdAt().toString());
        ZonedDateTime expectedUpdatedAt = ZonedDateTime.parse(aCategory.getUpdatedAt().toString());
        ZonedDateTime actualUpdatedAt = ZonedDateTime.parse(actualCategory.updatedAt().toString());

        // Truncar ambos para a mesma precisão (por exemplo, milissegundos)
        expectedCreatedAt = expectedCreatedAt.truncatedTo(ChronoUnit.MILLIS);
        actualCreatedAt = actualCreatedAt.truncatedTo(ChronoUnit.MILLIS);
        expectedUpdatedAt = expectedUpdatedAt.truncatedTo(ChronoUnit.MILLIS);
        actualUpdatedAt = actualUpdatedAt.truncatedTo(ChronoUnit.MILLIS);

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        //Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());  //Comentar caso esteja com divergência nos nanosegundos
        Assertions.assertEquals(expectedCreatedAt, actualCreatedAt); //Descomentar caso o teste de cima esteja com problemas
        //Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());  //Comentar caso esteja com divergência nos nanosegundos
        Assertions.assertEquals(expectedUpdatedAt, actualUpdatedAt); //Descomentar caso o teste de cima esteja com problemas
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound(){
        final var expetedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expetedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var expetedErrorMessage = "Gateway error";
        final var expectedId = CategoryID.from("123");

        Mockito.doThrow(new IllegalStateException(expetedErrorMessage))
                        .when(categoryGateway).findById(Mockito.eq(expectedId));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expetedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) { //Recebo uma lista
        categoryRepository.saveAllAndFlush(
            Arrays.stream(aCategory)
                    .map(CategoryJpaEntity::from)
                    .toList() //Criando uma lista de categorias
        );
    }



}
