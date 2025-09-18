package com.bart.example.infrastructure.templating.ports.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DependencyClass {
    private final String className;
    private final String simpleName;
    private final List<String> dependencies;
}
