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
}
