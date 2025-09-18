package com.bart.example.infrastructure.templating.usecases.mapper;

import com.bart.example.infrastructure.templating.commands.model.DispatchClassesCommandModel;
import com.bart.example.infrastructure.templating.commands.model.FullPathDispatchClassCommandModel;
import com.bart.example.infrastructure.templating.commands.model.HashCodeDispatchClassCommand;
import com.bart.example.infrastructure.templating.ports.model.DispatchClasses;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DispatchClassesCommandModelMapper {

    @Getter
    private static final DispatchClassesCommandModelMapper instance = new DispatchClassesCommandModelMapper();

    public DispatchClassesCommandModel mapToCommandModel(DispatchClasses dispatchClasses, List<FullPathDispatchClassCommandModel> fullPaths, List<HashCodeDispatchClassCommand> shortPaths) {
        return DispatchClassesCommandModel.builder()
                .filer(dispatchClasses.getFiler())
                .packageName(dispatchClasses.getPackageName())
                .fullPaths(fullPaths)
                .shortPaths(shortPaths)
                .className("InternalRouteDispatcher")
                .build();
    }
}
