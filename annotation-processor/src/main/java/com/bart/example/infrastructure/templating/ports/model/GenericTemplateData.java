package com.bart.example.infrastructure.templating.ports.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import javax.annotation.processing.Filer;

@SuperBuilder
public abstract class GenericTemplateData {
    public abstract TemplateType getTemplateType();

    @Getter
    public String packageName;

    @Getter
    private Filer filer;
}
