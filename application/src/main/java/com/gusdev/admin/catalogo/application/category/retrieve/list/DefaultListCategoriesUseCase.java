package com.gusdev.admin.catalogo.application.category.retrieve.list;

import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.category.CategorySeachQuery;
import com.gusdev.admin.catalogo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySeachQuery aQuery) {
        return this.categoryGateway.findAll(aQuery) //Pagination de 'Category'
                .map(CategoryListOutput::from); //Converto em um paginatio de 'CategoryListOutput'
    }
}
