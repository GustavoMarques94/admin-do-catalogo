package com.gusdev.admin.catalogo;

import com.gusdev.admin.catalogo.infrastructure.configuration.ObjectMapperConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
//Anotação padrão para uma classe de teste no spring
@WebMvcTest //Anoração semanticamente equivalente a @DataJpaTest, porém não tem o mesmo comportamento
@Import(ObjectMapperConfig.class)
public @interface ControllerTest {

    //Estou dizendo o @ControllerTest irá receber nele como parâmetro, um contrller, e ai o spring irá fazer o bind dessa anotação e passar para essa propriedade chamada controller, irá fazer um merge da informação
    @AliasFor(annotation = WebMvcTest.class, attribute = "controllers")
    Class<?>[] controllers() default {};


}
