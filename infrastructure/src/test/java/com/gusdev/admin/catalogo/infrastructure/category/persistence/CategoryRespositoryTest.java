package com.gusdev.admin.catalogo.infrastructure.category.persistence;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRespositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_WhenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var aCategory = Category.newCategory("Filmes","A categoria mais assistida",true);

        final var anEntity = CategoryJpaEntity.from(aCategory); //Criação da entidade
        anEntity.setName(null);

        //Irá retornar 'DataIntegrityViolationException' Exception genérica que o spring utiliza para encapsular um problema
        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity)); //Passando para o category repositpry para que ele salve

        //Dentro do 'DataIntegrityViolationException' tem um assert que é o exeception final do Hibernate, o 'PropertyValueException'
        //'PropertyValueException' é a exception de fato do Hibernate que está validando
        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_WhenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        final var aCategory = Category.newCategory("Filmes","A categoria mais assistida",true);

        final var anEntity = CategoryJpaEntity.from(aCategory); //Criação da entidade
        anEntity.setCreatedAt(null);

        //Irá retornar 'DataIntegrityViolationException' Exception genérica que o spring utiliza para encapsular um problema
        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity)); //Passando para o category repositpry para que ele salve

        //Dentro do 'DataIntegrityViolationException' tem um assert que é o exeception final do Hibernate, o 'PropertyValueException'
        //'PropertyValueException' é a exception de fato do Hibernate que está validando
        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_WhenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";
        final var aCategory = Category.newCategory("Filmes","A categoria mais assistida",true);

        final var anEntity = CategoryJpaEntity.from(aCategory); //Criação da entidade
        anEntity.setUpdatedAt(null);

        //Irá retornar 'DataIntegrityViolationException' Exception genérica que o spring utiliza para encapsular um problema
        final var actualException =
                Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity)); //Passando para o category repositpry para que ele salve

        //Dentro do 'DataIntegrityViolationException' tem um assert que é o exeception final do Hibernate, o 'PropertyValueException'
        //'PropertyValueException' é a exception de fato do Hibernate que está validando
        final var actualCause =
                Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}
