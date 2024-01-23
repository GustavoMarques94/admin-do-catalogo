package com.gusdev.admin.catalogo.infrastructure.category.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

//O JpaRepository recebe dois tipos com base em generics:
//1° parâmetro - Entidade que irá utilizar
//2° parâmetro - Tipo da propriedade do id de CategoryJpaEntity
public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {

    Page<CategoryJpaEntity> findAll(Specification<CategoryJpaEntity> whereClause, Pageable pageable);

    @Query(value = "select c.id from Category c where c.id in :ids")
    List<String> existeByIds(@Param("ids") List<String> ids);
}
