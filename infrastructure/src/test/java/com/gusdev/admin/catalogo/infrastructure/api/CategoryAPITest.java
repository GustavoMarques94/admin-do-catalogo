package com.gusdev.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gusdev.admin.catalogo.ControllerTest;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.gusdev.admin.catalogo.application.category.create.CreateCategoryUseCase;
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

    @Test
    public void givenAValidCommand_whenCallCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A melhor categoria de filmes";
        final var expectedIsActive = true;

        final var anInput =
                new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(CreateCategoryOutput.from("123"))); //Temos que retornar um Either de API.Right, e ele espera um retorno que é o retorno do nosso caso de uso 'CategoryOutput'

        final var request = MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON) //Json como tipo de conteúdo
                .content(this.mapper.writeValueAsString(anInput)); //Que Json iremos enviar? um conteúdo (CreateCategoryApiInput)
                //O 'writeValueAsString' lança uma 'Exception' então devemos especificar no método que ele faz um throws de Exception (Como é um método de teste não tem problema)

        this.mvc.perform(request) //Estou dizendo que ele irá performar uma ação, mas que ação? o 'request' instanciado a cima irá performar está requisição
                .andDo(MockMvcResultHandlers.print())
                //iremos passar uma lista de predicados
                .andExpect(MockMvcResultMatchers.status().isCreated()) //Essse é o cara que nós esperamos
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

}
