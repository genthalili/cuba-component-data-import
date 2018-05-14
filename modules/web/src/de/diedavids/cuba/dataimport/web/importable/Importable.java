package de.diedavids.cuba.dataimport.web.importable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Importable {
    String listComponent() default "";
    String buttonId() default "importDataBtn";
    String buttonsPanel() default "buttonsPanel";
}