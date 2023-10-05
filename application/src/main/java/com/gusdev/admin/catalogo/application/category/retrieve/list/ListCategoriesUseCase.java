package com.gusdev.admin.catalogo.application.category.retrieve.list;

import com.gusdev.admin.catalogo.application.UseCase;
import com.gusdev.admin.catalogo.domain.category.CategorySeachQuery;
import com.gusdev.admin.catalogo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySeachQuery, Pagination<CategoryListOutput>> {
}
