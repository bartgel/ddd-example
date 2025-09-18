package com.bart.example.infrastructure.webserving.commands;

import com.bart.example.infrastructure.templating.ports.TemplatingPort;
import com.bart.example.infrastructure.templating.ports.model.EndpointClass;
import com.bart.example.infrastructure.templating.ports.model.EndpointClasses;
import com.bart.example.infrastructure.templating.ports.model.EndpointMethodToDispatch;
import com.bart.example.infrastructure.webserving.models.HttpMethodData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import java.util.List;
import java.util.Map;

import static com.bart.example.infrastructure.webserving.services.ContextClassUtils.generateContextClassName;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateEndpointClassesCommand {
    @Getter
    private final static  GenerateEndpointClassesCommand instance = new GenerateEndpointClassesCommand();

    private final static TemplatingPort templatingPort = TemplatingPort.getInstance();

    public void execute (Map<String, List<HttpMethodData>> map, Filer filer) {
        templatingPort.generateEndpointClasses(
                EndpointClasses.builder()
                        .filer(filer)
                        .packageName("com.bart.example.infrastructure.webserving.commands")
                        .classes(map.entrySet().stream()
                                        .map(GenerateEndpointClassesCommand::extractEndpoints).toList()
                        ).build());
    }

    private static EndpointClass extractEndpoints(Map.Entry<String, List<HttpMethodData>> e) {
        return EndpointClass.builder()
                .className(generateContextClassName(e.getKey()))
                .methods(e.getValue().stream()
                                .map(GenerateEndpointClassesCommand::extractHttpMethods).toList()
                ).build();
    }

    private static EndpointMethodToDispatch extractHttpMethods(HttpMethodData value) {
        return EndpointMethodToDispatch.builder()
                .method(value.getHttpMethod())
                .dispatchClassName(value.getClassName())
                .dispatchMethodName(value.getMethodName())
                .returnType(value.getReturnType())
                .subType(value.getSubType())
                .build();
    }
}
