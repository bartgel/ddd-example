package com.bart.example.infrastructure.templating.commands.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HashCodeDispatchClassCommand {
    private int pathHashcode;
    private String targetClass;
    private String simpleClassName;
}
