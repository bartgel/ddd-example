package com.bart.example.infrastructure.webserving.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HttpMethodData {
    String httpMethod;
    String className;
    String methodName;
    String returnType;
    String subType;
}
