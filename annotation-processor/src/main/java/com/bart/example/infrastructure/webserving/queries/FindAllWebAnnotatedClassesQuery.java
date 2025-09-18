package com.bart.example.infrastructure.webserving.queries;

import com.bart.example.infrastructure.webserving.models.HttpMethodData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.*;
import java.util.*;

import static com.bart.example.infrastructure.webserving.services.PathComparatorUtil.removeTrailingSlash;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FindAllWebAnnotatedClassesQuery {
    @Getter
    private static final FindAllWebAnnotatedClassesQuery instance = new FindAllWebAnnotatedClassesQuery();


    public Map<String, List<HttpMethodData>> execute(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<String,List<HttpMethodData>> map = new HashMap<>();

        for (TypeElement annotation : annotations) {
            for (Element element : roundEnv.getElementsAnnotatedWith(annotation)) {
                if (element.getKind() == ElementKind.METHOD) {
                    ExecutableElement method = (ExecutableElement) element;
                    String path = getPath(method, annotation);

                    //System.out.println(method.getReturnType().);
                    HttpMethodData methodData =
                            HttpMethodData.builder()
                                    .httpMethod(getHttpMethod(annotation))
                                    .className(((TypeElement) method.getEnclosingElement()).getQualifiedName().toString())
                                    .methodName(method.getSimpleName().toString())
                                    .returnType(method.getReturnType().toString())
                                    .subType(getSubType(method.getReturnType().toString()))
                                    .build();

                    if (map.containsKey(path)) {
                        map.get(path).add(methodData);
                    } else {
                        map.put(path, new ArrayList<>(List.of(methodData)));
                    }
                }
            }
        }
        return map;
    }

    private String getSubType(String type) {
        if (type.startsWith("java.util.List<")) {
            return type.replaceAll("java.util.List<", "")
                    .replaceAll(">", "");
        }
        return "";
    }

    private String getHttpMethod(TypeElement annotation) {
        String annotationName = annotation.getSimpleName().toString();
        switch (annotationName) {
            case "GetMapping": return "GET";
            case "PostMapping": return "POST";
            case "PutMapping": return "PUT";
            case "DeleteMapping": return "DELETE";
            case "PatchMapping": return "PATCH";
            default: return "GET";
        }
    }

    private String getPath(ExecutableElement method, TypeElement annotation) {
        // Get the annotation value (path)
        for (AnnotationMirror ann : method.getAnnotationMirrors()) {
            if (ann.getAnnotationType().asElement().equals(annotation)) {
                for (ExecutableElement key : ann.getElementValues().keySet()) {
                    if (key.getSimpleName().toString().equals("value")) {
                        String path = ann.getElementValues().get(key).getValue().toString();
                        return removeTrailingSlash(path.replace("\"", "")); // Remove quotes
                    }
                }
            }
        }
        return "/"; // Default path
    }
}
