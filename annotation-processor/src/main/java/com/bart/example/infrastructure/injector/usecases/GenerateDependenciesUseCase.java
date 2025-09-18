package com.bart.example.infrastructure.injector.usecases;

import com.bart.example.infrastructure.injector.commands.GenerateDependencyClassCommand;
import com.bart.example.infrastructure.injector.queries.FindAllSingletonQuery;
import com.bart.example.infrastructure.injector.queries.SortAllByDependencyQuery;
import com.bart.example.infrastructure.injector.models.DependencyInjectorClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateDependenciesUseCase {
    @Getter
    private final static GenerateDependenciesUseCase instance = new GenerateDependenciesUseCase();

    GenerateDependencyClassCommand generateDependencyClassCommand =
            GenerateDependencyClassCommand.getInstance();
    FindAllSingletonQuery findAllSingletonQuery =
            FindAllSingletonQuery.getInstance();
    SortAllByDependencyQuery sortAllByDependencyQuery =
            SortAllByDependencyQuery.getInstance();

    public boolean execute(RoundEnvironment roundEnv, Filer filer) {
        List<DependencyInjectorClass> initializationOrder =
                Optional.of(findAllSingletonQuery.execute(roundEnv))
                        .map(sortAllByDependencyQuery::execute)
                        .orElse(Collections.emptyList());
        if (initializationOrder.isEmpty()) {
            return false;
        }
        generateDependencyClassCommand.execute(initializationOrder,filer);
        return true;
    }


}
