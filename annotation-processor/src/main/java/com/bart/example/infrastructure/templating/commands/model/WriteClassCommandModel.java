package com.bart.example.infrastructure.templating.commands.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.annotation.processing.Filer;

@SuperBuilder
public class WriteClassCommandModel {
    @Getter
    public String packageName;

    @Getter
    public String className;

    @Getter
    private Filer filer;

    @Getter
    @Setter
    public String content;
}
