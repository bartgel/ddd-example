package com.bart.example.infrastructure.templating.commands;

import com.bart.example.infrastructure.templating.ports.model.JsonBaseClass;
import com.bart.example.infrastructure.templating.ports.model.JsonClass;
import com.bart.example.infrastructure.templating.ports.model.JsonField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import static com.bart.example.infrastructure.templating.services.ClassMemberTransformationService.capitalize;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MakeContentJsonWriterCommand {
    @Getter
    private final static MakeContentJsonWriterCommand instance = new MakeContentJsonWriterCommand();

    private final static String JSON_WRITER_BASE = """
            package {0};

            import java.time.LocalDate;
            import java.time.LocalDateTime;
            import java.time.format.DateTimeFormatter;
            import com.bart.example.infrastructure.json.parsers.AbstractWriterParser;
            import java.util.*;
            import java.util.stream.Collectors;
            
            public class {1} extends AbstractWriterParser '{'
            {2}
            {3}
            '}'
            """;

    private final String TO_JSON = """
                public String toJson({0} obj) '{'
                   if (obj == null) return "null";
                   StringBuilder json = new StringBuilder();
            {1}
                   return json.toString();
                '}'
            """;

    private final String FUNCTION_ENCAPSULATION = """
                   json.append("'{'");
            {0}
                   json.append("'}'");
            """;

    private final String FIELD_ENCAPTULATOR = """
                   appendToJson(json, "{0}", obj.get{1}() == null, sb -> parseField(obj.get{1}(),sb)); 
            """;

    private final String JSON_LIST = """
                public String toJsonList{0}(List<{1}> obj) '{'
                    return obj == null
                       ?  "null"
                       : "[" + obj.stream().map(this::toJson).collect(Collectors.joining(",")) + "]";
                '}'
            """;


    public String execute(JsonClass jsonClass) {
        return MessageFormat.format(
                JSON_WRITER_BASE
                , jsonClass.getPackageName()
                , "JsonWriter"
                , jsonClass.getBaseClasses().stream()
                        .map(this::supportLists)
                        .collect(Collectors.joining())
                , jsonClass.getBaseClasses().stream()
                        .map(this::encapsulateClass)
                        .collect(Collectors.joining())
        );
    }

    private String supportLists(JsonBaseClass baseClass) {
        return MessageFormat.format(JSON_LIST, baseClass.getClassName(), baseClass.getFullClassName());
    }

    private String encapsulateClass (JsonBaseClass baseClass) {
        return MessageFormat.format
                (TO_JSON
                , baseClass.getFullClassName()
                , dataTypeConvertion(baseClass.getJsonFieldList())
                );
    }

    private String dataTypeConvertion (List<JsonField> jsonFields) {
        String fields =
                jsonFields.stream()
                        .map(s -> MessageFormat.format(
                                  FIELD_ENCAPTULATOR
                                , s.getFieldName()
                                , capitalize(s.getFieldName())
                   ))
                        .collect(Collectors.joining("       json.append(\",\");\n"));
        return MessageFormat.format
                (FUNCTION_ENCAPSULATION
                , fields);
    }
}
