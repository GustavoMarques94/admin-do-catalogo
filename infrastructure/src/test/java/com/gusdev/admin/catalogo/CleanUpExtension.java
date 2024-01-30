package com.gusdev.admin.catalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;

//Deletar todas as informações que foram previamente manipuladas pelo teste anterior, para garantir isolamente e um teste não impacte outro
public class CleanUpExtension implements BeforeEachCallback { //E como utilizar essa extensão? com a anotação @ExtendWith lá em cima

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
