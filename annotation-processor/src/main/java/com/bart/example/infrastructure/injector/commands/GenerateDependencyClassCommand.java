package com.bart.example.infrastructure.injector.commands;


import com.bart.example.infrastructure.templating.ports.TemplatingPort;
import com.bart.example.infrastructure.templating.ports.model.DependencyClass;
import com.bart.example.infrastructure.templating.ports.model.DependencyClasses;
import com.bart.example.infrastructure.injector.models.DependencyInjectorClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import java.util.List;

import static com.bart.example.infrastructure.injector.models.WebConstants.TARGET_PACKAGE_NAME;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateDependencyClassCommand {
    @Getter
    private final static GenerateDependencyClassCommand instance = new GenerateDependencyClassCommand();

    private final static TemplatingPort templatingPort = TemplatingPort.getInstance();

    public void execute(List<DependencyInjectorClass> dependencies, Filer filer) {
        DependencyClasses portDependencies =
                DependencyClasses.builder()
                        .filer(filer)
                        .packageName("com.bart.example.infrastructure.injector.ports")
                        .dependencyClassList
                                (dependencies.stream()
                                        .map(s ->
                                                DependencyClass.builder()
                                                .dependencies(s.getDependencies())
                                                .className(s.getClassName())
                                                .simpleName(s.getSimpleName())
                                                        .build()
                                                ).toList())
                                .build();
        templatingPort.generateInjectorClasses(portDependencies);
    }
}
