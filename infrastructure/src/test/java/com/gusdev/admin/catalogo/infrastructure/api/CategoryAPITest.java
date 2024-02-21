package com.gusdev.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gusdev.admin.catalogo.ControllerTest;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.gusdev.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.gusdev.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.gusdev.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.gusdev.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.gusdev.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.gusdev.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.gusdev.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryID;
import com.gusdev.admin.catalogo.domain.exceptions.DomainException;
import com.gusdev.admin.catalogo.domain.exceptions.NotFoundException;
import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import com.gusdev.admin.catalogo.domain.pagination.SearchQuery;
import com.gusdev.admin.catalogo.domain.validation.Error;
import com.gusdev.admin.catalogo.domain.validation.handler.Notification;
import com.gusdev.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.gusdev.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
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
import java.util.List;
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

    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

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
        final var expetedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expetedErrorMessage)));

    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() throws Exception{
        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON) //Digo que aceito o JSON
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
            Objects.equals(expectedName, cmd.name()) &&
                    Objects.equals(expectedDescription, cmd.description()) &&
                    Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() throws Exception{
        // given
        final var expectedId = "not-found"; //id inválido
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;

        final var expectedErrorMessage = "Category with ID not-found was not found";

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON) //Digo que aceito o JSON
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage)));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception{
        // given
        final var expectedId = "123";
        final String expectedName = null;
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;

        final var expectedMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedMessage))));

        final var aCommand =
                new UpdateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON) //Digo que aceito o JSON
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity())
                .andExpect(MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedMessage)));

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name()) &&
                        Objects.equals(expectedDescription, cmd.description()) &&
                        Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_shouldBeReturnNoContent() throws Exception{
        // given
        final var expectedId = "123";

        Mockito.doNothing()
                .when(deleteCategoryUseCase).execute(Mockito.any());

        // when
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON) //Digo que aceito o JSON
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
                //.andExpect(MockMvcResultMatchers.header().string("Content-Type",MediaType.APPLICATION_JSON_VALUE)); //Como é um NO_CONTENT, ele não setou esse HEADER por mais que nós tenhamos setado na interface

        Mockito.verify(deleteCategoryUseCase, Mockito.times(1)).execute(Mockito.eq(expectedId));
    }


    @Test
    public void givenAValidParams_whenCallsListCategories_shouldReturnCategories() throws Exception{
        // given
        final var aCategory = Category.newCategory("movies", null, true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;
        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        Mockito.when(listCategoriesUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("pagPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPage)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItemsCount)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aCategory.getId().getValue())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aCategory.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(aCategory.getDescription())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(aCategory.isActive())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].updated_at", Matchers.equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(aCategory.getDeletedAt())));

        Mockito.verify(listCategoriesUseCase, Mockito.times(1)).execute(Mockito.argThat(query ->
               Objects.equals(expectedPage, query.page()) &&
               Objects.equals(expectedPerPage, query.perPage()) &&
               Objects.equals(expectedDirection, query.direction()) &&
               Objects.equals(expectedSort, query.sort()) &&
               Objects.equals(expectedTerms, query.terms())
        ));
    }
}
