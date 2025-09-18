package com.bart.example.infrastructure.injector.queries;

import com.bart.example.infrastructure.injector.annotations.Singleton;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindAllSingletonQuery {
    @Getter
    private final static FindAllSingletonQuery instance = new FindAllSingletonQuery();

    public Set<TypeElement> execute(RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(Singleton.class)
                .stream()
                .filter(element -> element.getKind().isClass())
                .map(element -> (TypeElement) element)
                .collect(Collectors.toSet());
    }

}
