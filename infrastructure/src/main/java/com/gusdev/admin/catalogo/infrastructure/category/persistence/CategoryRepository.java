package com.gusdev.admin.catalogo.infrastructure.category.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

//O JpaRepository recebe dois tipos com base em generics:
//1° parâmetro - Entidade que irá utilizar
//2° parâmetro - Tipo da propriedade do id de CategoryJpaEntity
public interface CategoryRepository extends JpaRepository<CategoryJpaEntity, String> {

}
