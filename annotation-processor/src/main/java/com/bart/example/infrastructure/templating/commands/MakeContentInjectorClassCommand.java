package com.bart.example.infrastructure.templating.commands;

import com.bart.example.infrastructure.templating.commands.model.DependencyClassesCommandModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MakeContentInjectorClassCommand {
    public static final String DEPENDENCY_TEMPLATE = """
            package {0};
            
            import java.util.*;
            
            public class {1} '{'
            
            {2}
            
            {3}
            
            '}'
            """;

    private static final String MEMBERS =
            "    private static final {0} {1} = new {0}({2});";

    private static final String MEMBER_CONSTRUCTOR =
            "get{0}()";

    private static final String GETTERS = """
                public static {0} get{1} () '{'
                   return {2};
                '}'
            """;

    @Getter
    private final static MakeContentInjectorClassCommand instance = new MakeContentInjectorClassCommand();

    public String execute(DependencyClassesCommandModel dependencyClasses) {
        return MessageFormat.format
                        ( DEPENDENCY_TEMPLATE
                        , dependencyClasses.getPackageName()
                        , dependencyClasses.getClassName()
                        , getMembers(dependencyClasses)
                        , getGetters(dependencyClasses)
                  );
    }

    private String getGetters(DependencyClassesCommandModel dependencyClasses) {
        return dependencyClasses.getDependencies().stream()
                .map(d -> MessageFormat.format
                        ( GETTERS
                        , d.getClassName()
                        , d.getSimpleName()
                        , d.getFieldName()
                        )).collect(Collectors.joining("\n"));
    }

    private String getMembers(DependencyClassesCommandModel dependencyClasses) {
        return dependencyClasses.getDependencies().stream()
                .map(dep ->
                        MessageFormat.format
                                (MEMBERS
                                , dep.getClassName()
                                , dep.getFieldName()
                                , dep.getDependencyFields().stream()
                                        .map(field ->MessageFormat.format(MEMBER_CONSTRUCTOR, field))
                                        .collect(Collectors.joining(","))
                                )
                ).collect(Collectors.joining("\n"));
    }
}

