package com.bart.example.infrastructure.templating.ports.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class JsonField {
    private String fieldName;
    private JsonFieldType fieldType;
    private JsonBaseClass baseClass;
}
