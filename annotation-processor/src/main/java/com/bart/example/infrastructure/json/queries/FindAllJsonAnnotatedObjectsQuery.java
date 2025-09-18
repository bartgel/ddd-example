package com.bart.example.infrastructure.json.queries;


import com.bart.example.infrastructure.json.annotations.JsonObject;
import com.bart.example.infrastructure.templating.ports.model.JsonBaseClass;
import com.bart.example.infrastructure.templating.ports.model.JsonField;
import com.bart.example.infrastructure.templating.ports.model.JsonFieldType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindAllJsonAnnotatedObjectsQuery {
    @Getter
    private final static FindAllJsonAnnotatedObjectsQuery instance = new FindAllJsonAnnotatedObjectsQuery();

    public List<JsonBaseClass> execute(RoundEnvironment roundEnv) {
        List<JsonBaseClass> baseClasses = new ArrayList<>();

        for (Element element : roundEnv.getElementsAnnotatedWith(JsonObject.class)) {
            if (element instanceof TypeElement) {
                TypeElement typeElement = (TypeElement) element;
                baseClasses.add(
                        JsonBaseClass.builder()
                                .className(typeElement.getSimpleName().toString())
                                .fullClassName(typeElement.getQualifiedName().toString())
                                .jsonFieldList(
                                        getSerializableFields(typeElement).stream()
                                                .map(s -> JsonField.builder()
                                                        .fieldName(s.getSimpleName().toString())
                                                        .fieldType(getFieldType(s.asType()))
                                                        .build())
                                                .toList())
                                .build());
            }
        }
        return baseClasses;
    }


    private List<VariableElement> getSerializableFields(TypeElement typeElement) {
        List<VariableElement> fields = new ArrayList<>();
        for (Element element : typeElement.getEnclosedElements()) {
            if (element instanceof VariableElement && !element.getModifiers().contains(Modifier.STATIC)) {
                fields.add((VariableElement) element);
            }
        }
        return fields;
    }

    private JsonFieldType getFieldType (TypeMirror type) {
        switch (type.toString()) {
            case "java.lang.String": return JsonFieldType.STRING;
            case "java.time.LocalDate": return JsonFieldType.LOCAL_DATE;
            case "java.time.LocalDateTime": return JsonFieldType.LOCAL_DATE_TIME;
            case "java.lang.Integer":
            case "int":
                return JsonFieldType.INTEGER;
            case "java.lang.Double":
            case "double":
                return JsonFieldType.DOUBLE;
            case "java.lang.Long":
            case "long":
                return JsonFieldType.LONG;
            case "java.lang.Boolean":
            case "boolean":
                return JsonFieldType.BOOLEAN;
            case "java.util.List":
                return JsonFieldType.LIST;
        }
        return JsonFieldType.STRING;
    }


}
