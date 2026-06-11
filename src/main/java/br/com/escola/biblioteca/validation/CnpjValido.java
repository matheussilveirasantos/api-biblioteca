package br.com.escola.biblioteca.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CnpjValidador.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CnpjValido {
    String message() default "CNPJ inválido. Use o formato: XX.XXX.XXX/XXXX-XX";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
