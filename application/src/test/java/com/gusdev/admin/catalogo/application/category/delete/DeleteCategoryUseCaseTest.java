package com.gusdev.admin.catalogo.application.category.delete;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void clenUp(){
        Mockito.reset(categoryGateway);
    }

    //1. Teste do caminho feliz
    //2. Teste passando um ID que não é válido
    //3. Teste recebendo um erro do gateway

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeOk(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        //Como mockar o comportamento de um método que é void (não tem retorno)
        //Temos que explicitar o que será feito antes       ex: doNothing --> Não faça nada    when --> quando
        Mockito.doNothing()
                .when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        //Esse assert espera nenhuma Exception seja lançada
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        //Assert que espera que o 'categoryGateway' seja chamado apenas 1 vez no 'expectedId'
        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    //Este teste é "meio inútil" pois não estamos trabalhando com a persistência de fato, mas é interessante deixar ele explícito, para futuramente
        //quando houver uma implementação, saibamos o que fazer
    @Test
    public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOk(){
        final var expectedId = CategoryID.from("123"); //ID genérico, que não exista

        Mockito.doNothing()
                .when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_shouldReturnException(){
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(categoryGateway)
                .deleteById(Mockito.eq(expectedId));

        //assert que espera que venha uma 'IllegalStateException'
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
    }

}
