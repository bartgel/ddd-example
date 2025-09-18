package com.bart.example.infrastructure.templating.usecases;

import com.bart.example.infrastructure.templating.commands.MakeContentInjectorClassCommand;
import com.bart.example.infrastructure.templating.commands.WriteClassCommand;
import com.bart.example.infrastructure.templating.commands.model.DependencyClassCommandModel;
import com.bart.example.infrastructure.templating.commands.model.DependencyClassesCommandModel;
import com.bart.example.infrastructure.templating.ports.model.DependencyClasses;
import com.bart.example.infrastructure.templating.services.ClassMemberTransformationService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateDependencyInjectorUsecase {
    @Getter
    private final static CreateDependencyInjectorUsecase instance = new CreateDependencyInjectorUsecase();

    private final static MakeContentInjectorClassCommand generateDependencyInjectorCommand
            = MakeContentInjectorClassCommand.getInstance();
    private final static WriteClassCommand writeClassCommand = WriteClassCommand.getInstance();

    public void execute(DependencyClasses dependencyClasses) {
        DependencyClassesCommandModel dependencyClassModel =
           DependencyClassesCommandModel.builder()
                .className("CompileTimeDIContainer")
                .packageName(dependencyClasses.getPackageName())
                .filer(dependencyClasses.getFiler())
                .dependencies(
                        dependencyClasses.getDependencyClassList().stream()
                                .map(d -> DependencyClassCommandModel.builder()
                                        .className(d.getClassName())
                                        .simpleName(d.getSimpleName())
                                        .fieldName(ClassMemberTransformationService.getFieldName(d.getSimpleName()))
                                        .dependencies(d.getDependencies())
                                        .dependencyFields
                                                (d.getDependencies().stream()
                                                   .map(ClassMemberTransformationService::getSimpleClassName)
                                                  .toList())
                                        .build())
                                .toList())
                   .build();
        dependencyClassModel.setContent(generateDependencyInjectorCommand.execute(dependencyClassModel));
        writeClassCommand.execute(dependencyClassModel);
    }
}
