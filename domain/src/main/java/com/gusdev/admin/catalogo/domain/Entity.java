package com.gusdev.admin.catalogo.domain;

import java.util.Objects;

//Essa classe Entity serve como uma estrutura básica para entidades no sistema, ela tem um campo 'id'
    //para armazenar um identificador único (tipo genérico), e a classe é projetada para ser estendida
    //por outras classes que representam entidades específicas. Cada entidade que estende essa classe
    //terá um campo 'id' herdado e poderá definir comportamentos específicos e campos adicionais conforme
    //necessário. A classe 'Entity' em si não pode ser instanciada diretamente, ela serve como um esqueleto
    // para outras classes.
//ID é um tipo genérico que deve estender (ser uma subclasse de) Identifier
//Essa abordagem genérica permite que você use diferentes tipos de identificadores para diferentes tipos de entidades.
public abstract class Entity<ID extends Identifier> {

    //protected permite que as subclasses terão acesso ao campo id
    //id é um campo(variável de instância) do tipo ID que é do tipo genérico definido na classe
    protected final ID id;

    protected Entity(final ID id) {
        //Valido no construtor que um objeto não pode ser construído com valor nulo
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
    }

    public ID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o; //Converte o para entity
        return getId().equals(entity.getId()); //Compara os IDs
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
