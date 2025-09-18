package com.bart.example.infrastructure.templating.ports.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EndpointMethodToDispatch {
    String method;
    String dispatchClassName;
    String dispatchMethodName;
    String returnType;
    String subType;
}
