package com.gusdev.admin.catalogo.infrastructure.category;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.pagination.SearchQuery;
import com.gusdev.admin.catalogo.infrastructure.MySQLGatewayTest;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

//Classe que irá acoplar todos os testes do Gateway
@MySQLGatewayTest // All agregado que desejar fazer teste com o tipo MySQLGateway é só anotar com essa anotação que já estará configurado
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySqlGateway;

    @Autowired
    private CategoryRepository categoryRepository; //Através do repositório, podemos manipular as informações no BD sem ter que passar toda hora pelo gateway;iremos fazer um mockap antes

    @Test
    public void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        //Gero instância de uma nova categoria
        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        //Asserto que não tenha nada no banco antes de chamar o gateway
        Assertions.assertEquals(0, categoryRepository.count()); //Verifico se não tenhanada persistido no BD, para que nada interfira no teste

        //Chamo o gateway
        final var actualCategory = categoryMySqlGateway.create(aCategory);

        //Assert que tenha uma informação no banco
        Assertions.assertEquals(1, categoryRepository.count());

        //Assert do que retornou do gateway
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        //Assert do que de fato está persistido, para verificar se não foi inserido nenhum dado errôneo
        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated(){
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        //Gero instância de uma nova categoria com valores inválidos
        final var aCategory = Category.newCategory("Film", null, expectedIsActive);

        //Asserto para verificar que não tenha nada no banco antes de chamar o gateway
        Assertions.assertEquals(0, categoryRepository.count()); //Verifico se não tenhanada persistido no BD, para que nada interfira no teste

        //Persistismo a categoria com valores inválidos, para que possamos fazer a atualização
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

        //Assert para verificar que tenha uma informação no banco
        Assertions.assertEquals(1, categoryRepository.count());

        //Busco a entidade criada no BD com as informações erradas e verifico se elas realmente foram persistidas erradas
        final var actualInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
        Assertions.assertEquals("Film", actualInvalidEntity.getId());
        Assertions.assertNull(actualInvalidEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualInvalidEntity.isActive());

        //Faço um clone chamando uma nova instância de categoria para não refletir na primeira instância
        final var aUpdatedCategory = aCategory.clone()
                .update(expectedName, expectedDescription, expectedIsActive);

        //Chamo o gateway
        final var actualCategory = categoryMySqlGateway.update(aUpdatedCategory);

        //Assert para verificar que ainda tenha apenas uma informação no BD
        Assertions.assertEquals(1, categoryRepository.count());

        //Assert do que retornou do gateway
        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualCategory.getCreatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        //Assert do que de fato está persistido, para verificar se não foi inserido nenhum dado errôneo
        final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(actualEntity.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }








    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated(){
        final var filme = Category.newCategory("Filmes", "The category most watched", true);
        final var serie = Category.newCategory("Series", "The most fun series", true);
        final var documentary = Category.newCategory("Documentaries", "A vision of the reality", true);

        categoryRepository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(filme),
                CategoryJpaEntity.from(serie),
                CategoryJpaEntity.from(documentary)
        ));

        final var query = new SearchQuery(1, 1, "", "name", "desc");
        final var categories = categoryMySqlGateway.findAll(query);

        Assertions.assertEquals(filme.getId().getValue(), categories.items().get(0).getId().getValue());
        Assertions.assertEquals(3, categories.total());
        Assertions.assertEquals(1, categories.items().size());
        Assertions.assertEquals("Filmes", categories.items().get(0).getName());
        Assertions.assertEquals("The category most watched", categories.items().get(0).getDescription());
        Assertions.assertTrue(categories.items().get(0).isActive());

    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage(){
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;
        Assertions.assertEquals(0, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryMySqlGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(0, actualResult.items().size());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated(){
        final var expectedPage = 1;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;
        final var filme = Category.newCategory("Filmes", "The category most watched", true);
        final var serie = Category.newCategory("Series", "The most fun series", true);
        final var documentary = Category.newCategory("Documentaries", "A vision of the reality", true);

        categoryRepository.saveAllAndFlush(List.of(
                CategoryJpaEntity.from(filme),
                CategoryJpaEntity.from(serie),
                CategoryJpaEntity.from(documentary)
        ));

        Assertions.assertEquals(3, categoryRepository.count());
        final var query = new SearchQuery(1, 1, "", "name", "asc");

        final var actualResult = categoryMySqlGateway.findAll(query);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(1, actualResult.items().size());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(actualResult.items().get(0).getName(), filme.getName());
        Assertions.assertEquals(actualResult.items().get(0).getId().getValue(), filme.getId().getValue());
        Assertions.assertEquals(actualResult.items().get(0).getDescription(), filme.getDescription());
    }

}

