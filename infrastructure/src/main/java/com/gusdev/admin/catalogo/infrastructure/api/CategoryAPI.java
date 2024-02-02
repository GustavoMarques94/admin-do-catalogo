package com.gusdev.admin.catalogo.infrastructure.api;

//Vamos difinir quais o métodos a API irá expor, e também a parte de documentação com springDocs

import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categories") //tanto faz colocar com / ou sem /, o spring entende e vai tratar isso
public interface CategoryAPI {

    //ResponseEntity genérico <?>
    @PostMapping( //Estou dizendo que ele consome e produz Json
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    //@ResponseStatus(HttpStatus.CREATED) //É uma boa prática inserir o código de retorno, porém como estamos retornando um ResponseEntity não precisa declarar, pois o statusCod Http está no ResponseEntity
    ResponseEntity<?> createCategory();

    @GetMapping
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String dir
    );
}
