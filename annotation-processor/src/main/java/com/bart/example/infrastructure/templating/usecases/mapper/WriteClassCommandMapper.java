package com.bart.example.infrastructure.templating.usecases.mapper;

import com.bart.example.infrastructure.templating.commands.model.WriteClassCommandModel;
import com.bart.example.infrastructure.templating.ports.model.GenericTemplateData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WriteClassCommandMapper {
    @Getter
    private final static WriteClassCommandMapper instance = new WriteClassCommandMapper();

    public WriteClassCommandModel.WriteClassCommandModelBuilder getBuilder(GenericTemplateData templateData) {
        return WriteClassCommandModel.builder()
                .packageName(templateData.getPackageName())
                .filer(templateData.getFiler());
    }
}
