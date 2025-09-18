package com.bart.example.infrastructure.templating.usecases;

import com.bart.example.infrastructure.templating.commands.MakeContentRouteDispatcherCommand;
import com.bart.example.infrastructure.templating.commands.WriteClassCommand;
import com.bart.example.infrastructure.templating.commands.model.DispatchClassesCommandModel;
import com.bart.example.infrastructure.templating.commands.model.FullPathDispatchClassCommandModel;
import com.bart.example.infrastructure.templating.commands.model.HashCodeDispatchClassCommand;
import com.bart.example.infrastructure.templating.ports.model.DispatchClass;
import com.bart.example.infrastructure.templating.ports.model.DispatchClasses;
import com.bart.example.infrastructure.templating.services.ClassMemberTransformationService;
import com.bart.example.infrastructure.templating.usecases.mapper.DispatchClassesCommandModelMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateDispatchClassesUsecase {
    @Getter
    private final static CreateDispatchClassesUsecase instance = new CreateDispatchClassesUsecase();
    private final static MakeContentRouteDispatcherCommand GENERATE_ROUTE_DISPATCHER_COMMAND = MakeContentRouteDispatcherCommand.getInstance();
    private final static WriteClassCommand writeClassCommand = WriteClassCommand.getInstance();
    private final static DispatchClassesCommandModelMapper mapper = DispatchClassesCommandModelMapper.getInstance();

    public void execute(DispatchClasses dispatchClasses) {
        DispatchClassesCommandModel model = processDispatchClasses(dispatchClasses);
        generateRouterDispatcher(model);
    }

    private void generateRouterDispatcher(DispatchClassesCommandModel model) {
        model.setContent(GENERATE_ROUTE_DISPATCHER_COMMAND.execute(model));
        writeClassCommand.execute(model);
    }

    private DispatchClassesCommandModel processDispatchClasses(DispatchClasses dispatchClasses) {
        Map<Boolean, List<DispatchClass>> partitioned = dispatchClasses.getClasses().stream()
                .collect(Collectors.partitioningBy(this::requiresPathParsing));

        List<FullPathDispatchClassCommandModel> fullPaths = partitioned.get(true).stream()
                .map(this::createFullPathModel)
                .collect(Collectors.toList());

        List<HashCodeDispatchClassCommand> shortPaths = partitioned.get(false).stream()
                .map(this::createHashCodeModel)
                .collect(Collectors.toList());

        validateHashCodeUniqueness(shortPaths);

        return mapper.mapToCommandModel(dispatchClasses, fullPaths, shortPaths);
    }

    private FullPathDispatchClassCommandModel createFullPathModel(DispatchClass dispatchClass) {
        return FullPathDispatchClassCommandModel.builder()
                .targetClass(dispatchClass.getTargetClass())
                .pathName(dispatchClass.getPathName())
                .simpleClassName(ClassMemberTransformationService.getSimpleClassName(dispatchClass.getTargetClass()))
                .build();
    }

    private HashCodeDispatchClassCommand createHashCodeModel(DispatchClass dispatchClass) {
        int hashCode = dispatchClass.getPathName().hashCode();
        return HashCodeDispatchClassCommand.builder()
                .pathHashcode(hashCode)
                .targetClass(dispatchClass.getTargetClass())
                .simpleClassName(ClassMemberTransformationService.getSimpleClassName(dispatchClass.getTargetClass()))
                .build();
    }

    private void validateHashCodeUniqueness(List<HashCodeDispatchClassCommand> shortPaths) {
        Set<Integer> seenHashes = new HashSet<>();
        for (HashCodeDispatchClassCommand command : shortPaths) {
            if (!seenHashes.add(command.getPathHashcode())) {
                throw new RuntimeException(
                        "Duplicate hashcode detected for path: " + command.getPathHashcode()
                );
            }
        }
    }

    private boolean requiresPathParsing(DispatchClass dispatchClass) {
        return dispatchClass.getPathName().contains("{");
    }
}
