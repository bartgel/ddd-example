package com.bart.example.infrastructure.templating.ports.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class EndpointClasses extends GenericTemplateData {
    @Override
    public TemplateType getTemplateType() {
        return TemplateType.ENDPOINT_ENTRY;
    }

    @Getter
    @Setter
    private List<EndpointClass> classes;
}
