package com.gusdev.admin.catalogo.infrastructure.category;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.infrastructure.MySQLGatewayTest;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//Classe que irá acoplar todos os testes do Gateway
@MySQLGatewayTest // All agregado que desejar fazer teste com o tipo MySQLGateway é só anotar com essa anotação que já estará configurado
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

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
        final var actualCategory = categoryGateway.create(aCategory);

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



}

