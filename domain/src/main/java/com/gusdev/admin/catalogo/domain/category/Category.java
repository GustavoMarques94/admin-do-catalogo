package com.gusdev.admin.catalogo.domain.category;

import com.gusdev.admin.catalogo.domain.AggregateRoot;
import com.gusdev.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.UUID;

//A classe Category está estendendo (herdando) a classe genérica AggregateRoot e especificando que o tipo genérico
    //associado a essa herança é CategoryID.  Isso implica que a classe Category herda comportamentos e
    //propriedades da classe AggregateRoot, e está sendo parametrizada pelo tipo CategoryID.
//<CategoryID> é o identificador único para a categoria.
public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID anid, final String aName, final String aDescription, final boolean isActive,
                    final Instant aCreatedAt, final Instant aUpdatedAt, final Instant aDeletedAt) {
        super(anid); //Construtor da classe pai 'AggregateRoot'
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.deletedAt = aDeletedAt;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean aIsActive){
        final var id = CategoryID.unique(); // ID gerado randomicamente pela JVM
        final var now = Instant.now();
        return new Category(id, aName, aDescription, aIsActive, now, now, null);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
