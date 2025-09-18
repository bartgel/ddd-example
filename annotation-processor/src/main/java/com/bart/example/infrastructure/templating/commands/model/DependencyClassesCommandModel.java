package com.bart.example.infrastructure.templating.commands.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class DependencyClassesCommandModel extends WriteClassCommandModel {
    @Getter
    private List<DependencyClassCommandModel> dependencies;
}
