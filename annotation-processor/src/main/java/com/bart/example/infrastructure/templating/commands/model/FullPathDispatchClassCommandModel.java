package com.bart.example.infrastructure.templating.commands.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FullPathDispatchClassCommandModel {
    String pathName;
    String targetClass;
    String simpleClassName;
}
