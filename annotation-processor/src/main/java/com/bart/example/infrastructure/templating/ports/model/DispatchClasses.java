package com.bart.example.infrastructure.templating.ports.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static com.bart.example.infrastructure.templating.ports.model.TemplateType.DISPATCH_CLASS;

@SuperBuilder
public class DispatchClasses extends  GenericTemplateData{
    @Override
    public TemplateType getTemplateType() {
        return DISPATCH_CLASS;
    }

    @Getter
    private List<DispatchClass> classes;
}
