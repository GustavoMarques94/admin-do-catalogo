package com.gusdev.admin.catalogo.domain.category;

import com.gusdev.admin.catalogo.domain.AggregateRoot;
import com.gusdev.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

//A classe Category está estendendo (herdando) a classe genérica AggregateRoot e especificando que o tipo genérico
    //associado a essa herança é CategoryID.  Isso implica que a classe Category herda comportamentos e
    //propriedades da classe AggregateRoot, e está sendo parametrizada pelo tipo CategoryID.
//<CategoryID> é o identificador único para a categoria.
//Cloeable -> é um comportamento da JVM de pegar os atributos desse objeto, criar um novo objeto e copiar os
    //atribudos para esse novo objeto, em vez de copiar apenas a referência
public class Category extends AggregateRoot<CategoryID> implements Cloneable {

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

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive){
        final var id = CategoryID.unique(); // ID gerado randomicamente pela JVM
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);
    }

    public static Category with(
            final CategoryID from,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        return new Category(
                from,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category with(final Category aCategory){
        return with(
                aCategory.getId(),
                aCategory.name,
                aCategory.description,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        //Se já tiver um valor, de repente estou desativando uma categoria já desativada, não faz sentido mudar essa data.
        if(Objects.isNull(getDeletedAt())){
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category activate() {
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();

        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive) {
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();

        if(isActive){
            activate();
        }else{
            deactivate();
        }

        return this;
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

    @Override
    public Category clone(){
        try{
            return (Category) super.clone();
        }catch (CloneNotSupportedException e){
            throw new AssertionError();
        }
    }

}
