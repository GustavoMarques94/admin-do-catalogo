package com.gusdev.admin.catalogo.infrastructure.category.models;

import com.gusdev.admin.catalogo.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import java.time.Instant;

@JacksonTest //Notação que criamos para realizar testes de serialização
public class CategoryResponseTest {

    @Autowired
    private JacksonTester<CategoryResponse> json; //Propriedade que queremos testar

    @Test
    public void testeMarshall() throws Exception{
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeleteddAt = Instant.now();

        final var response = new CategoryResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeleteddAt
        );

        final var actualJson = this.json.write(response); //Vamos escrever essa response em uma String

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString())
                .hasJsonPathValue("$.deleted_at", expectedDeleteddAt.toString());

    }

    @Test
    public void testeUnmarshall() throws Exception{
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
                {
                    "id": "%s",
                    "name": "%s",
                    "description": "%s",
                    "is_active": "%s",
                    "created_at": "%s",
                    "updated_at": "%s",
                    "deleted_at": "%s"
                }
                """.formatted(
                    expectedId,
                    expectedName,
                    expectedDescription,
                    expectedIsActive,
                    expectedCreatedAt,
                    expectedUpdatedAt,
                    expectedDeletedAt
                );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                //o 1° parâmetro tem que está igual na propriedade da record 'CategoryResponse'
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt);
    }
}
