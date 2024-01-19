package com.gusdev.admin.catalogo.infrastructure.category;

//Implementação de fato do Gateway
//

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.category.CategoryID;
import com.gusdev.admin.catalogo.domain.category.CategorySeachQuery;
import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //Podemos anotar com @Component ou @Service, depende da perspectiva, fazem a mesma coisa
public class CategoryMySQLGateway implements CategoryGateway { //devo implementar todos os métodos definidos pela interface CategoryGateway

    private final CategoryRepository repository;

    public CategoryMySQLGateway(final CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(Category aCategory) {
        return null;
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
