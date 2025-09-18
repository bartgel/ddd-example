package com.bart.example.infrastructure.templating.ports.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class DependencyClasses  extends GenericTemplateData {
    @Override
    public TemplateType getTemplateType() {
        return TemplateType.DEPENDENCY_CLASS;
    }

    @Getter
    public List<DependencyClass> dependencyClassList;
}
