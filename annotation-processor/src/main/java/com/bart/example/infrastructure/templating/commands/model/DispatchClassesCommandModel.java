package com.bart.example.infrastructure.templating.commands.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class DispatchClassesCommandModel extends WriteClassCommandModel {
    @Getter
    List<FullPathDispatchClassCommandModel> fullPaths;
    @Getter
    List<HashCodeDispatchClassCommand> shortPaths;

    public void setClassName (String className) {
        this.className = className;
    }
}
