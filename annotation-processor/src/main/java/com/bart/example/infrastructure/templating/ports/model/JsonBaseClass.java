package com.bart.example.infrastructure.templating.ports.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class JsonBaseClass {
    String className;
    String fullClassName;
    List<JsonField> jsonFieldList;
}
