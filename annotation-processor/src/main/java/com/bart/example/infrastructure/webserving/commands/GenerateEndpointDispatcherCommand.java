package com.bart.example.infrastructure.webserving.commands;

import com.bart.example.infrastructure.templating.ports.TemplatingPort;
import com.bart.example.infrastructure.templating.ports.model.DispatchClass;
import com.bart.example.infrastructure.templating.ports.model.DispatchClasses;
import com.bart.example.infrastructure.webserving.models.HttpMethodData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import java.util.List;
import java.util.Map;

import static com.bart.example.infrastructure.webserving.models.WebConstants.TARGET_PACKAGE_NAME;
import static com.bart.example.infrastructure.webserving.services.ContextClassUtils.generateContextClassName;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GenerateEndpointDispatcherCommand {
    @Getter
    private final static  GenerateEndpointDispatcherCommand instance = new GenerateEndpointDispatcherCommand();

    private final static TemplatingPort templatingPort = TemplatingPort.getInstance();

    public void execute (Map<String, List<HttpMethodData>> map, Filer filer) {
        templatingPort.generateDispatchClasses(
                DispatchClasses.builder()
                        .filer(filer)
                        .packageName("com.bart.example.infrastructure.webserving.ports")
                        .classes(map.keySet().stream()
                                .map(entry -> DispatchClass.builder()
                                        .pathName(entry)
                                        .targetClass(generateContextClassName(entry)).build())
                                .toList())
                        .build());

    }
}
