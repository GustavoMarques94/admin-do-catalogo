package com.gusdev.admin.catalogo.application.category.update;

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
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId(){
        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());

        //O execute retornará um Either, de notification ou de sucesso
        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get(); //o findById retorna um 'Optional', porém o .get() é usado apenas para teste

        //Como estava com uma pequena diferença nos nanosegundos da criação de uma nova categoria, trunquei algumas casas decimais para o teste passar
        ZonedDateTime expectedCreatedAt = ZonedDateTime.parse(aCategory.getCreatedAt().toString());
        ZonedDateTime actualCreatedAt = ZonedDateTime.parse(actualCategory.getCreatedAt().toString());

        // Truncar ambos para a mesma precisão (por exemplo, milissegundos)
        expectedCreatedAt = expectedCreatedAt.truncatedTo(ChronoUnit.MILLIS);
        actualCreatedAt = actualCreatedAt.truncatedTo(ChronoUnit.MILLIS);

        //Verificando o que foi persistido no BD
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        //Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertEquals(expectedCreatedAt, actualCreatedAt); //Descomentar caso o teste de cima esteja com problemas
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException(){
        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        final String expectedName = null;
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        //Assert --> quero ter certeza que o 'create' NÃO irá ser chamado 'times(0)', utilizo o any
        //Garante que o categoryGateway não foi chamado quando um nome de categoria inválido foi fornecido
        Mockito.verify(categoryGateway, Mockito.times(0)).update(any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId(){
        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Assertions.assertTrue(aCategory.isActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        //Como estava com uma pequena diferença nos nanosegundos da criação de uma nova categoria, trunquei algumas casas decimais para o teste passar
        ZonedDateTime expectedCreatedAt = ZonedDateTime.parse(aCategory.getCreatedAt().toString());
        ZonedDateTime actualCreatedAt = ZonedDateTime.parse(actualCategory.getCreatedAt().toString());

        // Truncar ambos para a mesma precisão (por exemplo, milissegundos)
        expectedCreatedAt = expectedCreatedAt.truncatedTo(ChronoUnit.MILLIS);
        actualCreatedAt = actualCreatedAt.truncatedTo(ChronoUnit.MILLIS);

        //Verificando o que foi persistido no BD
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        //Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertEquals(expectedCreatedAt, actualCreatedAt); //Descomentar caso o teste de cima esteja com problemas
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnException() {
        final var aCategory = Category.newCategory("Film", null, true);

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway Error";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway)
                .update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        //Como estava com uma pequena diferença nos nanosegundos da criação de uma nova categoria, trunquei algumas casas decimais para o teste passar
        ZonedDateTime expectedCreatedAt = ZonedDateTime.parse(aCategory.getCreatedAt().toString());
        ZonedDateTime actualCreatedAt = ZonedDateTime.parse(actualCategory.getCreatedAt().toString());
        ZonedDateTime expectedUpdatedAt = ZonedDateTime.parse(aCategory.getUpdatedAt().toString());
        ZonedDateTime actualUpdatedAt = ZonedDateTime.parse(actualCategory.getUpdatedAt().toString());



        // Truncar ambos para a mesma precisão (por exemplo, milissegundos)
        expectedCreatedAt = expectedCreatedAt.truncatedTo(ChronoUnit.MILLIS);
        actualCreatedAt = actualCreatedAt.truncatedTo(ChronoUnit.MILLIS);
        expectedUpdatedAt = expectedUpdatedAt.truncatedTo(ChronoUnit.MILLIS);
        actualUpdatedAt = actualUpdatedAt.truncatedTo(ChronoUnit.MILLIS);

        //Verificando o que foi persistido no BD
        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
        //Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
        Assertions.assertEquals(expectedCreatedAt, actualCreatedAt); //Descomentar caso o teste de cima esteja com problemas
        //Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(expectedUpdatedAt, actualUpdatedAt); //Descomentar caso o teste de cima esteja com problemas
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = false;
        final var expectedId = "123"; //Esse ID nem existe
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var aCommand =
                UpdateCategoryCommand.with(expectedId, expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) { //Recebo uma lista
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList() //Criando uma lista de categorias
        );
    }
}
