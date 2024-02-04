package com.gusdev.admin.catalogo.infrastructure.api;

//Vamos difinir quais o métodos a API irá expor, e também a parte de documentação com springDocs

import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping(value = "categories") //tanto faz colocar com / ou sem /, o spring entende e vai tratar isso
@Tag(name = "Categories") //Nome do Resource que estamos espondo
public interface CategoryAPI {

    //ResponseEntity genérico <?>
    @PostMapping( //Estou dizendo que ele consome e produz Json
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    //@ResponseStatus(HttpStatus.CREATED) //É uma boa prática inserir o código de retorno, porém como estamos retornando um ResponseEntity não precisa declarar, pois o statusCod Http está no ResponseEntity
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
           @ApiResponse(responseCode = "201", description = "Created successfully"),
           @ApiResponse(responseCode = "422", description = "A validation error was thrown"),
           @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    ResponseEntity<?> createCategory();

    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Listed successfully"),
            @ApiResponse(responseCode = "422", description = "A invalid parameter was received"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    Pagination<?> listCategories(
            @RequestParam(name = "search", required = false, defaultValue = "") final String search,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "name") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String dir
    );
}
