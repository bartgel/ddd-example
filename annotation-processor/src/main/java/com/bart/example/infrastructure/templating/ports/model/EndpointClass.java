package com.bart.example.infrastructure.templating.ports.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class EndpointClass {
    private String className;
    private EndpointClasses endpointClasses;
    List<EndpointMethodToDispatch> methods;
}
