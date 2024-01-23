package com.gusdev.admin.catalogo.infrastructure.category;

//Implementação de fato do Gateway

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.category.CategoryID;
import com.gusdev.admin.catalogo.domain.category.CategorySeachQuery;
import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //Podemos anotar com @Component ou @Service, depende da perspectiva, fazem a mesma coisa
public class CategoryMySQLGateway implements CategoryGateway { //devo implementar todos os métodos definidos pela interface CategoryGateway

    private final CategoryRepository repository; //O repository que se comunica de fato com o hibernate, faz toda comunição (é o ORM)

    public CategoryMySQLGateway(final CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category aCategory) {
        //Geramos um JpaEntity de categoria a partir do agregado, manda salvar, e o que retornar converte para toAggregate
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(CategoryID anId) {

    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        return Optional.empty();
    }

    @Override
    public Category update(Category aCategory) {
        return null;
    }

    @Override
    public Pagination<Category> findAll(CategorySeachQuery aQuery) {
        return null;
    }
}
