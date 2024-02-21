package com.gusdev.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

//Criamos esse record para: ser quem esperamos no nosso contrato com a API
//Poderia configurar o ObjectMapper da aplicação para fazer isso? Sim, poderiamos, porém
    //as classes record ainda não estão funcionando muito bem com o bind de snakeCase (ex: snake_case)
    //com o ObjectMapper, então seria mais interessante declarar o próprio @JsonProperty
    //especificando qual é o nome da propriedade
public record UpdateCategoryRequest(
        //o @JsonProperty define como a anotação faz o bind com o Json
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active
) {
}
