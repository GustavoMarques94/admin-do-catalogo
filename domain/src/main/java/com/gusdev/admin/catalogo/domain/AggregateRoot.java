package com.gusdev.admin.catalogo.domain;

//Essa classe  é uma extensão especializada da classe Entity, destinada a representar "agregados raiz".
    //Ela fornece um construtor que permite definir o identificador ao criar um novo agregado raiz.
    //As subclasses desta classe podem adicionar mais comportamentos específicos de agregados raiz, se necessário.
//'AggregateRoot' é uma subclasse da classe 'Entity<ID>'
    //Isso significa que ela herda os campos e comportamentos definidos na classe Entity, incluindo o campo id.
//ID é um tipo genérico que deve estender Identifier. Isso significa que essa classe permite que você
    //crie agregados raiz onde o identificador é um tipo específico que estende a classe 'Identifier'
public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        //chama o construtor da classe pai ('Entity<ID>'), assim a classe pai pode inicializar o campo id da entidade
        super(id);
    }
}
