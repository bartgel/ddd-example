package com.bart.example.infrastructure.json.commands;

import com.bart.example.infrastructure.templating.ports.TemplatingPort;
import com.bart.example.infrastructure.templating.ports.model.JsonBaseClass;
import com.bart.example.infrastructure.templating.ports.model.JsonClass;
import com.bart.example.infrastructure.webserving.models.WebConstants;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.Filer;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateJsonReaderCommand {
    @Getter
    private final static CreateJsonReaderCommand instance = new CreateJsonReaderCommand();
    private static TemplatingPort templatingPort = TemplatingPort.getInstance();

    public void execute(Filer filer, List<JsonBaseClass> baseClasses) {
        JsonClass jsonClass = JsonClass.builder()
                .baseClasses(baseClasses)
                .filer(filer)
                .packageName("com.bart.example.infrastructure.json.ports")
                .build();

        templatingPort.generateJsonReader(jsonClass);
    }



}
