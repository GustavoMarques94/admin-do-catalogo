package com.gusdev.admin.catalogo.domain.category;

import com.gusdev.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

//Define um conjunto de métodos que uma classe que deseje manipular categorias deve implementar
//Esses métodos geralmente são usados para interagir com algum tipo de repositório ou serviço relacionado a categorias
//Isso permite a criação de diferentes implementações que podem trabalhar com BD, serviços web, ou
    //outras fontes de dados, mantendo um contrato comum.
public interface CategoryGateway {

    //Recebe uma nova categoria e retorna uma instância de 'Category' que foi criada
    Category create(Category aCategory);

    void deleteById(CategoryID anId);

    //Busca uma categoria com base no ID da categoria e retorna um Optional<Category>, o Optional é usado para
        //indicar que a categoria pode ou não ser encontrada, evitando problema com valores nulos
    Optional<Category> findById(CategoryID anId);

    Category update(Category aCategory);

    //<étodo utilizado para encontrar todas as categorias com base em um objeto 'CategorySeachQuery'
    //Ele retorna um objeto de 'Pagination<Category>', que contém informações sobre... olhar o record 'Pagination'
    //'CategorySeachQuery' e 'Pagination' são utilizados para passar informações de pesquisa e paginação
    Pagination<Category> findAll(CategorySeachQuery aQuery);
}
