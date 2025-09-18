package com.bart.example.infrastructure.templating.commands.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DependencyClassCommandModel {
    private String className;
    private String simpleName;
    private String fieldName;
    private List<String> dependencies;
    private List<String> dependencyFields;
}
