package com.gusdev.admin.catalogo.domain;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import com.gusdev.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

//Esse teste verifica se a classe Category lida corretamente com a criação de uma categoria com nome nulo,
    //garantindo que uma exceção seja lançada com a mensagem de erro esperada.
public class CategoryTest {
    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_theShouldReciveErrorInstantiateCategory(){
        final String expectedName = null;
        final var expectedErrorCount = 1; //Especifico que espero um único erro
        final var expectedErrorMessage = "'name' should not be null"; //Defino a msg de erro esperada
        final var expectedDescription = "Categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                //O método assertThrows verifica se uma exceção do tipo 'DomainException' é lançada quando
                //'actualCategory.validate()' é chamado. Isso verifica se a validação correta foi implementada.
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));
                //1°PARAM - Classe da exceção que você espera que seja lançada
                //2°PARAM - () -> expressão lambda, usava para criar uma função anônima que pode ser passada
                    //como argumento para métodos ou atribuída a variáveis
                    //validate() é um método na classe Category que verifica se a categoria é válida,
                    //lançando uma exceção DomainException se não for.

        //Verifico se o número de erros da exceção corresponde ao valor esperado
        Assertions.assertEquals(expectedErrorCount, actualException.getErros().size());
        //Verifico se a mensagem de erro na exceção corresponde à mensagem esperada
        Assertions.assertEquals(expectedErrorMessage, actualException.getErros().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_theShouldReciveErrorInstantiateCategory(){
        final var expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedDescription = "Categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        //Verifico se o número de erros da exceção corresponde ao valor esperado
        Assertions.assertEquals(expectedErrorCount, actualException.getErros().size());
        //Verifico se a mensagem de erro na exceção corresponde à mensagem esperada
        Assertions.assertEquals(expectedErrorMessage, actualException.getErros().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_theShouldReciveErrorInstantiateCategory(){
        final var expectedName = "Gu ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characteres";
        final var expectedDescription = "Categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        //Verifico se o número de erros da exceção corresponde ao valor esperado
        Assertions.assertEquals(expectedErrorCount, actualException.getErros().size());
        //Verifico se a mensagem de erro na exceção corresponde à mensagem esperada
        Assertions.assertEquals(expectedErrorMessage, actualException.getErros().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_theShouldReciveErrorInstantiateCategory(){
        final var expectedName = """
            Evidentemente, o desafiador cenário globalizado deve passar por modificações independentemente do 
            sistema de formação de quadros que corresponde às necessidades. Assim mesmo, a contínua expansão de 
            nossa atividade assume importantes posições no estabelecimento de alternativas às soluções ortodoxas.
            """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characteres";
        final var expectedDescription = "Categoria mais assistida";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        //Verifico se o número de erros da exceção corresponde ao valor esperado
        Assertions.assertEquals(expectedErrorCount, actualException.getErros().size());
        //Verifico se a mensagem de erro na exceção corresponde à mensagem esperada
        Assertions.assertEquals(expectedErrorMessage, actualException.getErros().get(0).message());
    }

    @Test
    public void givenAValidEmptyDescription_whenCallNewCategoryAndValidate_theShouldReciveErrorInstantiateCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = " ";
        final var expectedIsActive = true;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidFalseIsActive_whenCallNewCategoryAndValidate_theShouldReciveErrorInstantiateCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }
}
