package com.gusdev.admin.catalogo.application.category.create;

import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;

//Classe de teste unitário para a classe 'DefaultCreateCategoryUseCase' utilizando o framework de teste Mockito
//Esses testes garantem que o 'CreateCategoryUseCase' se comporta conforme esperado em diferentes situações, como entradas válidas, entradas inválidas e cenários de erro do gateway.
//Isso ajuda a garantir a robustez do código e a manter o comportamento esperado da lógica de criação de categoria.

//Notação usada para congfigurar a execução dos testes com o Mockito
//utilizada em conjunto com a notação '@Mock' para criar mocks dos objetos necessários no teste
@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    //Notação usada para injetar objetos @mocks nas propriedades da classe de teste 'DefaultCreateCategoryUseCase'
    //O Mockito irá injetar automaticamente as instâncias mockadas nas propriedades marcadas com '@InjectMocks'
    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    //Essa notação cria um mock (objeto simulado) de 'CategoryGateway'
    @Mock
    private CategoryGateway categoryGateway;

    //1. Teste do caminho feliz
    //2. Teste passando propriedade inválida (name)
    //3. Teste criando uma categoria invativa
    //4. Teste simulando um erro genérico vindo do gateway

    //1.Testa o caso de uso com um comando de categoria válido.
        //Verifica se o CreateCategoryUseCase cria uma categoria corretamente e chama o método create no
        //CategoryGateway com os parâmetros corretos.
    @Test
    public void givenAValidCommand_whenCallCreateCategory_shouldReturnCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;
        //Objeto 'CreateCategoryCommand ' é criado, um comando válido para criar uma categoria
        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);
        //Configuro o Mockito para quando o método 'create' do 'categoryGateway' for chamado,
            //com qualquer argumento/objeto 'any()', então deve retornar o 1° argumento que foi passado pra ele
        Mockito.when(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        //Método execute do caso de uso é chamado com o comando de criação de categoria
        //Retorna um identificador (objeto 'CategoryID') de categoria válido
        final var actualOutput = useCase.execute(aCommand);

        //1°Assert
        Assertions.assertNotNull(actualOutput);
        //2°Assert
        Assertions.assertNotNull(actualOutput.id());
        //3°Assert com verify do mockito
        //Verifica se o método 'create' do 'categoryGateway' oi chamado exatamente 1 vez 'times(1)' com os
            //argumentos esperados, representados pelo 'argThat' e verifica se os valores abaixo esperados foram
            //passados ao método create
        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                        return Objects.equals(expectedName, aCategory.getName())
                                && Objects.equals(expectedDescription, aCategory.getDescription())
                                && Objects.equals(expectedIsActive, aCategory.isActive())
                                && Objects.nonNull(aCategory.getId())
                                && Objects.nonNull(aCategory.getCreatedAt())
                                && Objects.nonNull(aCategory.getUpdatedAt())
                                && Objects.isNull(aCategory.getDeletedAt());
                    }
                ));
    }

    //2.Testa o caso de uso com um comando de categoria inválido (nome nulo)
        //Garante que uma exceção de domínio (DomainException) seja lançada.
    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException(){
        final String expectedName = null;
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        //'execute' do caso de uso é chamado com o comando de criação de categoria que tem o nome nulo
        //'assertThrows' verifica se uma exceção do tipo 'DomainException' é lançada durante a execução do caso de uso
        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        //Assert --> quero ter certeza que o 'create' NÃO irá ser chamado 'times(0)', utilizo o any
        //Garante que o categoryGateway não foi chamado quando um nome de categoria inválido foi fornecido
        Mockito.verify(categoryGateway, Mockito.times(0)).create(any());
    }

    //3. Testa o caso de uso com um comando de categoria válida, mas inativa.
        //Verifica se o 'CreateCategoryUseCase' cria uma categoria inativa e chama o método create no
        //'CategoryGateway' com os parâmetros corretos.
    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(categoryGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand);

        //1°Assert
        Assertions.assertNotNull(actualOutput);
        //2°Assert
        Assertions.assertNotNull(actualOutput.id());
        //3°Assert com verify do mockito
        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(expectedName, aCategory.getName())
                                    && Objects.equals(expectedDescription, aCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aCategory.isActive())
                                    && Objects.nonNull(aCategory.getId())
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.nonNull(aCategory.getDeletedAt());
                        }
                ));
    }

    //4.Testa o caso de uso quando o 'CategoryGateway' lança uma exceção.
        //Verifica se o 'CreateCategoryUseCase' lança a exceção corretamente.
    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnException(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "Gateway Error";

        final var aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        //Mockito é configurado para quando o método 'create' do 'categoryGateway' for chamado com qualquer
            //argumento 'any()' ele deve lançar uma exceção 'IllegalStateException' com a mensagem "Gateway Error"
            //Isso simula um cenário em que o 'categoryGateway' falha ao criar uma categoria e lança uma exceção
        Mockito.when(categoryGateway.create(any()))
                .thenThrow(new IllegalStateException("Gateway Error"));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(expectedName, aCategory.getName())
                                    && Objects.equals(expectedDescription, aCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aCategory.isActive())
                                    && Objects.nonNull(aCategory.getId())
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.isNull(aCategory.getDeletedAt());
                        }
                ));
    }
}
