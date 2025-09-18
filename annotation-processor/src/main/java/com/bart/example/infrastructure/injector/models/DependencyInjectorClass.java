package com.bart.example.infrastructure.injector.models;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@Builder
public class DependencyInjectorClass {
    private final String className;
    private final String simpleName;
    private final List<String> dependencies;
}
