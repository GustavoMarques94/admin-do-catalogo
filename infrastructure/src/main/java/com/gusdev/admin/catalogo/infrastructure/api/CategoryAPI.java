package com.gusdev.admin.catalogo.infrastructure.api;

//Vamos difinir quais o métodos a API irá expor, e também a parte de documentação com springDocs

import com.gusdev.admin.catalogo.domain.pagination.Pagination;
import com.gusdev.admin.catalogo.infrastructure.category.models.CategoryApiOutput;
import com.gusdev.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.gusdev.admin.catalogo.infrastructure.category.models.UpdateCategoryApiInput;
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
    ResponseEntity<?> createCategory(@RequestBody CreateCategoryApiInput input); //Informo na assinatura do método que ele irá receber um Json chamado Input; E como o Spring faz o bing do Json que veio do Body da requisição para um Objeto? '@RequestBody'


    @GetMapping
    @Operation(summary = "List all categories paginated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listed successfully"),
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

    @GetMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Get a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Category was not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown"),
    })
    CategoryApiOutput getById(@PathVariable(name = "id") String id); //Informo na assinatura do método que ele irá receber id, o @PathVariable irá fazer o bind do id para String

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Update a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "404", description = "Category wat not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    ResponseEntity<?> updateById(@PathVariable(name = "id") String id, @RequestBody UpdateCategoryApiInput input);

    @DeleteMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT) // Tipo de resposta
    @Operation(summary = "Delete a category by it's identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category wat not found"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    void deleteById(@PathVariable(name = "id") String id);
}
