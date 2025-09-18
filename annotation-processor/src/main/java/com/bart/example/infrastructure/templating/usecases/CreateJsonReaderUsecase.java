package com.bart.example.infrastructure.templating.usecases;

import com.bart.example.infrastructure.templating.commands.MakeContentJsonReaderCommand;
import com.bart.example.infrastructure.templating.commands.WriteClassCommand;
import com.bart.example.infrastructure.templating.commands.model.WriteClassCommandModel;
import com.bart.example.infrastructure.templating.ports.model.JsonClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateJsonReaderUsecase {
    @Getter
    private final static CreateJsonReaderUsecase instance = new CreateJsonReaderUsecase();

    private static MakeContentJsonReaderCommand makeContentJsonReaderCommand
            = MakeContentJsonReaderCommand.getInstance();

    private final WriteClassCommand writeClassCommand
            = WriteClassCommand.getInstance();

    public void execute(JsonClass jsonClass) {
        WriteClassCommandModel command = WriteClassCommandModel.builder()
                .filer(jsonClass.getFiler())
                .packageName(jsonClass.getPackageName())
                .className("JsonReader")
                .content(makeContentJsonReaderCommand.execute(jsonClass))
                .build();
        writeClassCommand.execute(command);
    }
}
