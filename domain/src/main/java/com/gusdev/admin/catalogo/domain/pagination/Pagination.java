package com.gusdev.admin.catalogo.domain.pagination;

import java.util.List;

//é um tipo genérico, os tipos genéricos permitem criar classes ou métodos que podem operar com
    //diferentes tipos de dados sem saber exatamente qual é o tipo de dados em tempo de compilação.
    //O <T> é um marcador que indica que a classe Pagination pode ser parametrizada com um tipo específico,
    // que será determinado quando você criar uma instância da classe.
public record Pagination<T>(
        int currentPage, //página atual
        int perPage, //quantidade de itens por página
        long total, //total de resultados disponíveis
        List<T> items //lista que contém os itens da página atua
) {
}
