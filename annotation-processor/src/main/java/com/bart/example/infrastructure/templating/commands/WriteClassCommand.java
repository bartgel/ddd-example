package com.bart.example.infrastructure.templating.commands;

import com.bart.example.infrastructure.templating.commands.model.WriteClassCommandModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.text.MessageFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WriteClassCommand {
    @Getter
    private static final WriteClassCommand instance = new WriteClassCommand();

    public void execute(WriteClassCommandModel model) {
        try {
            JavaFileObject builderFile =
                    model.getFiler()
                         .createSourceFile(getClassLocation(model));
            try (Writer writer = builderFile.openWriter()) {
                writer.write(model.getContent());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to write generated file " + getClassLocation(model));
        }
    }

    private String getClassLocation (WriteClassCommandModel model) {
        return MessageFormat.format("{0}.{1}", model.getPackageName(), model.getClassName());
    }
}
