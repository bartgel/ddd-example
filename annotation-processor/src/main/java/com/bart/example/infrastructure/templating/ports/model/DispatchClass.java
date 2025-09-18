package com.bart.example.infrastructure.templating.ports.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DispatchClass {
    String pathName;
    String targetClass;
}
