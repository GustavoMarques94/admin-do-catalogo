package com.gusdev.admin.catalogo.infrastructure;

import com.gusdev.admin.catalogo.infrastructure.category.CategoryMySQLGatewayTest;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE) //Digo para JVM quando e como utilizar as anotações
@Retention(RetentionPolicy.RUNTIME)
@Inherited //Caso alguém precise extender o comportamente dessa anotação
@ActiveProfiles("test") //Ativo o perfil de test para ele configurar o H2
//@SpringBootTest //Problema ao utilizar essa notação, o spring irá subir o contexto inteiro, absoluto; E esse não é nosso objetivo nesse momento, essa anotação é bom para testes integrados (end-to-end)
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[MySQLGateway]")
}) //Digo para o Spring o que é para ele configurar além do que o #DataJpaTest configurou
//Esse tipo de filtro é um macete, vai ser através de REGEX, digo que quero buscar todas as classes que terminam com MySQLGateway
@DataJpaTest //Esse teste configura somente o necessário para testar o repositório //Problema de usar o DataJpaTest é que ele não enxerga o nosso gateway que está anotado com @service, porém ele é muito mais rápido
@ExtendWith(MySQLGatewayTest.CleanUpExtensions.class) //Extendo a classe 'CleanUpExtensions'
public @interface MySQLGatewayTest {

    //Deletar todas as informações que foram previamente manipuladas pelo teste anterior, para garantir isolamente e um teste não impacte outro
    class CleanUpExtensions implements BeforeEachCallback { //E como utilizar essa extensão? com a anotação @ExtendWith lá em cima

        @Override
        public void beforeEach(final ExtensionContext context) throws Exception {
            final var repositories = SpringExtension
                    .getApplicationContext(context)
                    .getBeansOfType(CrudRepository.class) // 'CrudRepository' Interface mais alto nível; iremos precisar do método deleteAll();
                    .values();

            cleanUp(repositories); //Chamo cleanUP para todos os Repositórios que foram encontrados
        }

        private void cleanUp(final Collection<CrudRepository> repositories) {
            repositories.forEach(CrudRepository::deleteAll); //Chamo forEach passando 'DeleteAll' como Method Reference
        }
    }
}
