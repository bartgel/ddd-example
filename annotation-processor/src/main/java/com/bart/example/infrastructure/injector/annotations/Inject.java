package com.bart.example.infrastructure.injector.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE) // SOURCE retention for compile-time processing
@Target(ElementType.TYPE)
public @interface Inject {
    Class<?>[] dependencies() default {};
}
