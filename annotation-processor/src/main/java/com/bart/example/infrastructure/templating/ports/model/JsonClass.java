package com.bart.example.infrastructure.templating.ports.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class JsonClass extends  GenericTemplateData {
    @Override
    public TemplateType getTemplateType() {
        return TemplateType.JSON_CLASS;
    }

    @Getter
    private List<JsonBaseClass> baseClasses;
}
