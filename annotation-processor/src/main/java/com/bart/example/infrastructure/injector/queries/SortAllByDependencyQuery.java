package com.bart.example.infrastructure.injector.queries;

import com.bart.example.infrastructure.injector.models.DependencyInjectorClass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import java.util.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SortAllByDependencyQuery {
    @Getter
    private final static SortAllByDependencyQuery instance = new SortAllByDependencyQuery();

    public List<DependencyInjectorClass> execute(Set<TypeElement> singletonClasses) {
        Map<String, DependencyInjectorClass> classInfoMap = new HashMap<>();

        classInfoMap.put("com.bart.example.infrastructure.json.ports.JsonWriter"
                , DependencyInjectorClass.builder()
                        .className("com.bart.example.infrastructure.json.ports.JsonWriter")
                        .simpleName("JsonWriter")
                        .dependencies(Collections.emptyList())
                .build());
        classInfoMap.put("com.bart.example.infrastructure.json.ports.JsonReader"
                , DependencyInjectorClass.builder()
                        .className("com.bart.example.infrastructure.json.ports.JsonReader")
                        .simpleName("JsonReader")
                        .dependencies(Collections.emptyList())
                        .build());
        classInfoMap.put("com.bart.example.infrastructure.webserving.ports.InternalRouteDispatcher"
                , DependencyInjectorClass.builder()
                        .className("com.bart.example.infrastructure.webserving.ports.InternalRouteDispatcher")
                        .simpleName("InternalRouteDispatcher")
                        .dependencies(Collections.emptyList())
                        .build());

        // First pass: collect basic class info
        for (TypeElement classElement : singletonClasses) {
            DependencyInjectorClass dependencyInjectorClass = new DependencyInjectorClass(
                    classElement.getQualifiedName().toString(),
                    getSimpleClassName(classElement),
                    analyzeConstructorDependencies(classElement)
            );
            classInfoMap.put(dependencyInjectorClass.getClassName(), dependencyInjectorClass);
        }

        // Second pass: topological sort
        return topologicalSort(new ArrayList<>(classInfoMap.values()));
    }

    private List<String> analyzeConstructorDependencies(TypeElement classElement) {
        List<String> dependencies = new ArrayList<>();

        for (Element enclosed : classElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) enclosed;

                for (VariableElement param : constructor.getParameters()) {
                    TypeMirror paramType = param.asType();
                    dependencies.add(paramType.toString());
                }
                break; // Use first @Inject constructor
            }
        }

        return dependencies;
    }

    private List<DependencyInjectorClass> topologicalSort(List<DependencyInjectorClass> classes) {
        List<DependencyInjectorClass> result = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        Set<String> temp = new HashSet<>();

        for (DependencyInjectorClass dependencyInjectorClass : classes) {
            if (!visited.contains(dependencyInjectorClass.getClassName())) {
                visit(dependencyInjectorClass, classes, visited, temp, result);
            }
        }

        Collections.reverse(result);
        return result;
    }

    private void visit(DependencyInjectorClass dependencyInjectorClass, List<DependencyInjectorClass> allClasses,
                       Set<String> visited, Set<String> temp, List<DependencyInjectorClass> result) {
        if (temp.contains(dependencyInjectorClass.getClassName())) {
            throw new RuntimeException("Circular dependency detected: " + dependencyInjectorClass.getClassName());
        }

        if (!visited.contains(dependencyInjectorClass.getClassName())) {
            temp.add(dependencyInjectorClass.getClassName());

            for (String dependency : dependencyInjectorClass.getDependencies()) {
                DependencyInjectorClass depInfo = findClassInfo(dependency, allClasses);
                if (depInfo != null) {
                    visit(depInfo, allClasses, visited, temp, result);
                }
            }

            temp.remove(dependencyInjectorClass.getClassName());
            visited.add(dependencyInjectorClass.getClassName());
            result.add(dependencyInjectorClass);
        }
    }

    private DependencyInjectorClass findClassInfo(String className, List<DependencyInjectorClass> allClasses) {
        return allClasses.stream()
                .filter(info -> info.getClassName().equals(className))
                .findFirst()
                .orElse(null);
    }

    private String getSimpleClassName(TypeElement classElement) {
        String fullName = classElement.getQualifiedName().toString();
        return fullName.substring(fullName.lastIndexOf('.') + 1);
    }
}
