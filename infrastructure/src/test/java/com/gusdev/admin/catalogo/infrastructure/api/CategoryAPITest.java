package com.gusdev.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gusdev.admin.catalogo.ControllerTest;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.gusdev.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.gusdev.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryID;
import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import com.gusdev.admin.catalogo.domain.validation.Error;
import com.gusdev.admin.catalogo.domain.validation.handler.Notification;
import com.gusdev.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryAPITest {

    //Injetamos o MockMvc, tod o teste de cotroller, iremos utilizar o MockMvc
    //Ele irá nos auxiliar a configurar e fazer chamadas REST para os controllers que estamos passado na anotação lá em cima 'CategotyAPI.class'
    //Quem faz a configuração dele é o '@WebMvcTest'
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean //estou dizendo pro spring que quero que ele cria uma versão desse Bean totalmente mockavel para o spring injetar no controller
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    //cenário de teste feliz, quando tudo está ok
    @Test
    public void givenAValidCommand_whenCallCreateCategory_shouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;

        final var anInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123"))); //Temos que retornar um Either de API.Right, e ele espera um retorno que é o retorno do nosso caso de uso 'CategoryOutput'

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON) //Json como tipo de conteúdo
                .content(this.mapper.writeValueAsString(anInput)); //Que Json iremos enviar? um conteúdo (CreateCategoryApiInput)
                //O 'writeValueAsString' lança uma 'Exception' então devemos especificar no método que ele faz um throws de Exception (Como é um método de teste não tem problema)

        final var response = this.mvc.perform(request) //Estou dizendo que ele irá performar uma ação, mas que ação? o 'request' instanciado a cima irá performar está requisição
                .andDo(MockMvcResultHandlers.print());

        //  then
        response.andExpect(MockMvcResultMatchers.status().isCreated()) //Essse é o cara que nós esperamos
                .andExpect(MockMvcResultMatchers.header().string("Location", "/categories/123")) //O que espero  chegar no header
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123")));

        //Estou dizendo que espero que 'createCategoryUseCase' tenha sido chamado apenas uma vez o método execute, (linha 49)
        //Com isso iremos verificar o argumento que ele recebeu com argThat, fazendo um verify do argumento recebido
        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    //cenário de teste ruim, retorno uma notificação
    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var anInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessage)))); //Temos que retornar um Either de API.Left, caso de erro, como setamos o name como null, então é esse o erro que esperamos

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON) //Json como tipo de conteúdo
                .content(this.mapper.writeValueAsString(anInput)); //Que Json iremos enviar? um conteúdo (CreateCategoryApiInput)
        //O 'writeValueAsString' lança uma 'Exception' então devemos especificar no método que ele faz um throws de Exception (Como é um método de teste não tem problema)

        final var response = this.mvc.perform(request) //Estou dizendo que ele irá performar uma ação, mas que ação? o 'request' instanciado a cima irá performar está requisição
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity()) //Essse é o cara que nós esperamos
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue())) //O que espero  chegar no header
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage)));

        //Estou dizendo que espero que 'createCategoryUseCase' tenha sido chamado apenas uma vez o método execute, (linha 49)
        //Com isso iremos verificar o argumento que ele recebeu com argThat, fazendo um verify do argumento recebido
        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    //cenário de teste ruim, retorno uma Exception
    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;
        final var expectedMessage = "'name' should not be null";

        final var anInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedMessage))); //Lanço uma Exception quando bater no create

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON) //Json como tipo de conteúdo
                .content(this.mapper.writeValueAsString(anInput)); //Que Json iremos enviar? um conteúdo (CreateCategoryApiInput)
        //O 'writeValueAsString' lança uma 'Exception' então devemos especificar no método que ele faz um throws de Exception (Como é um método de teste não tem problema)

        final var response = this.mvc.perform(request) //Estou dizendo que ele irá performar uma ação, mas que ação? o 'request' instanciado a cima irá performar está requisição
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity()) //Essse é o cara que nós esperamos
                .andExpect(MockMvcResultMatchers.header().string("Location", Matchers.nullValue())) //O que espero  chegar no header
                .andExpect(MockMvcResultMatchers.header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedMessage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage)));

        //Estou dizendo que espero que 'createCategoryUseCase' tenha sido chamado apenas uma vez o método execute, (linha 49)
        //Com isso iremos verificar o argumento que ele recebeu com argThat, fazendo um verify do argumento recebido
        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() throws Exception{
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;

        final var aCategory =
                Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectedId = aCategory.getId().getValue();

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenReturn(CategoryOutput.from(aCategory));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON) //Digo que aceito o JSON
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));

        Mockito.verify(getCategoryByIdUseCase, Mockito.times(1)).execute(Mockito.eq(expectedId));
    }

    @Test
    public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() throws Exception{
        // given
        final var expetedErrorMessage = "Category with ID 123 was not-found";
        final var expectedId = CategoryID.from("123").getValue();

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(
                        new Error("Category with ID %s was not-found".formatted(expectedId))
                ));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expetedErrorMessage)));

    }

}
