package com.bart.example.infrastructure.templating.usecases;

import com.bart.example.infrastructure.templating.commands.MakeContentEndpointHandlerClassCommand;
import com.bart.example.infrastructure.templating.commands.WriteClassCommand;
import com.bart.example.infrastructure.templating.ports.model.EndpointClasses;
import com.bart.example.infrastructure.templating.usecases.mapper.WriteClassCommandMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateEndpointClassesUsecase {
    @Getter
    private static CreateEndpointClassesUsecase instance = new CreateEndpointClassesUsecase();

    private final MakeContentEndpointHandlerClassCommand generateEndpointClassCommand = MakeContentEndpointHandlerClassCommand.getInstance();
    private final WriteClassCommand writeClassCommand = WriteClassCommand.getInstance();
    private final WriteClassCommandMapper classCommandMapper = WriteClassCommandMapper.getInstance();

    public void execute (EndpointClasses endpointClasses) {
        endpointClasses.getClasses()
                        .forEach(s -> s.setEndpointClasses(endpointClasses));

        endpointClasses.getClasses().stream()
                .map(cs -> classCommandMapper.getBuilder(endpointClasses)
                        .className(cs.getClassName())
                        .content(generateEndpointClassCommand.execute(cs))
                        .build())
                .forEach(writeClassCommand::execute);
    }
}
