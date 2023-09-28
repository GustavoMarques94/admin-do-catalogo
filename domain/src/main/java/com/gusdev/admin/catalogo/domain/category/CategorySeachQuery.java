package com.gusdev.admin.catalogo.domain.category;

//Um DTO imutável para passar informações de um lado para o outro
//Record --> os registros são tipos de dados imutáveis que são usados principalmente para armazenar dados
    //Não tem comportamento
    //Seus valores não podem ser alterados após a criação de uma instância
public record CategorySeachQuery(
        int page, //página atual de resultados de pesquisa
        int perPage, //quantidade de itens por página
        String terms, //termos de pesquisa que podem ser usados para filtrar categorias
        String sort, //campo pelo qual as categorias devem ser ordenadas
        String direction //direção da ordenação, ascendente ou descendente
) {
}
