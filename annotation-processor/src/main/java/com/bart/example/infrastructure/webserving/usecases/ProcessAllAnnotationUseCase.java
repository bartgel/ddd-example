package com.bart.example.infrastructure.webserving.usecases;

import com.bart.example.infrastructure.webserving.commands.GenerateEndpointClassesCommand;
import com.bart.example.infrastructure.webserving.commands.GenerateEndpointDispatcherCommand;
import com.bart.example.infrastructure.webserving.models.HttpMethodData;
import com.bart.example.infrastructure.webserving.queries.FindAllWebAnnotatedClassesQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProcessAllAnnotationUseCase {
    @Getter
    private final static ProcessAllAnnotationUseCase instance = new ProcessAllAnnotationUseCase();

    private FindAllWebAnnotatedClassesQuery findAllWebAnnotatedClassesQuery = FindAllWebAnnotatedClassesQuery.getInstance();
    private GenerateEndpointClassesCommand generateEndpointClassesCommand = GenerateEndpointClassesCommand.getInstance();
    private GenerateEndpointDispatcherCommand generateEndpointDispatcherCommand = GenerateEndpointDispatcherCommand.getInstance();

    public void execute(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv, Filer filer) {
        Map<String, List<HttpMethodData>> map = findAllWebAnnotatedClassesQuery.execute(annotations, roundEnv);
        generateEndpointClassesCommand.execute(map,filer);
        generateEndpointDispatcherCommand.execute(map,filer);
    }
}
