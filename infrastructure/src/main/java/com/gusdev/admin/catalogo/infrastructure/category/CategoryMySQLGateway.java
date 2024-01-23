package com.gusdev.admin.catalogo.infrastructure.category;

import com.gusdev.admin.catalogo.domain.category.Category;
import com.gusdev.admin.catalogo.domain.category.CategoryGateway;
import com.gusdev.admin.catalogo.domain.category.CategoryID;
import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import com.gusdev.admin.catalogo.domain.pagination.SearchQuery;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.gusdev.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.gusdev.admin.catalogo.infrastructure.category.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

//Implementação de fato do Gateway
@Service //Podemos anotar com @Component ou @Service, depende da perspectiva, fazem a mesma coisa
public class CategoryMySQLGateway implements CategoryGateway { //devo implementar todos os métodos definidos pela interface CategoryGateway

    private final CategoryRepository repository; //O repository que se comunica de fato com o hibernate, faz toda comunição (é o ORM)

    public CategoryMySQLGateway(final CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(final CategoryID anId) {
        final String id = anId.getValue();
        //if (this.repository.existeByIds(id)) {
            this.repository.deleteById(id);
        //}
    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        return Optional.empty();
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(SearchQuery query) {
        PageRequest pageRequest = PageRequest.of(
                query.page(),
                query.perPage(),
                Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );
        final var specifications = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecificcation)
                .orElse(null);
        final var pageResult = this.repository.findAll(Specification.where(specifications), pageRequest);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CategoryID> existByIds(Iterable<CategoryID> ids) {
        final var categoryIds = StreamSupport.stream(ids.spliterator(), false)
                .map(CategoryID::getValue)
                .toList();

        return this.repository.existeByIds(categoryIds)
                .stream()
                .map(CategoryID::from)
                .toList();
    }

    private Specification<CategoryJpaEntity> assembleSpecificcation(String str) {
        final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name", str);
        final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description", str);
        return nameLike.or(descriptionLike);
    }

    private Category save(final Category aCategory) {
        //Geramos um JpaEntity de categoria a partir do agregado, manda salvar, e o que retornar converte para toAggregate
        return this.repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

}
