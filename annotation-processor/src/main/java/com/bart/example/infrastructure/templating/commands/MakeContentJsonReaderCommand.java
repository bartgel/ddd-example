package com.bart.example.infrastructure.templating.commands;

import com.bart.example.infrastructure.templating.ports.model.JsonBaseClass;
import com.bart.example.infrastructure.templating.ports.model.JsonClass;
import com.bart.example.infrastructure.templating.ports.model.JsonField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.text.MessageFormat;
import java.util.stream.Collectors;

import static com.bart.example.infrastructure.templating.services.ClassMemberTransformationService.capitalize;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MakeContentJsonReaderCommand {
    @Getter
    private final static MakeContentJsonReaderCommand instance = new MakeContentJsonReaderCommand();

    private final static String JSON_READ_BASE = """
            package {0};

            import java.time.LocalDate;
            import java.time.LocalDateTime;
            import java.time.format.DateTimeFormatter;
            import com.bart.example.infrastructure.json.parsers.AbstractReaderParser;
            import java.util.*;
            import java.util.stream.Collectors;
            
            public class {1} extends AbstractReaderParser '{'
            {2}
            {3}
            '}'
            """;

    private final String TO_OBJECT = """
                public {0} to{1}(String jsonString) '{'
                    if (isNull(jsonString)) return null;
                    {0} obj = new {0}();
                    return fromJson( obj, jsonString, (key,value) -> setFieldValue(obj, key, value));
                '}'
            """;

    private final static String TO_CASE = """
                       case "{0}" :
                         obj.set{1}(to{2}(value));
                         return;
            """;

      private final static String GENERATE_FIELD_VALUE = """
                 private void setFieldValue({0} obj, String key, String value) '{'
                     if (isNull(value)) return;
                     switch (key) '{'
              {1}
                        default:
                          return;
                     '}'
                 '}'
              """;

    public String execute(JsonClass jsonClass) {
        return MessageFormat.format(
                JSON_READ_BASE
                , jsonClass.getPackageName()
                , "JsonReader"
                , jsonClass.getBaseClasses().stream()
                        .map(this::generateMethod)
                        .collect(Collectors.joining())
                ,jsonClass.getBaseClasses().stream()
                        .map(this::generateFieldValue)
                        .collect(Collectors.joining()));

    }

    public String generateMethod(JsonBaseClass baseClass) {
        return MessageFormat.format
                (TO_OBJECT, baseClass.getFullClassName(), baseClass.getClassName());
    }

    public String generateFieldValue(JsonBaseClass baseClass) {
        return MessageFormat.format
                ( GENERATE_FIELD_VALUE
                , baseClass.getFullClassName()
                ,baseClass.getJsonFieldList().stream()
                          .map(this::mapFields)
                         .collect(Collectors.joining("\n"))
                   );
    }

    private String mapFields(JsonField jsonField) {
        return MessageFormat.format(TO_CASE
                 , jsonField.getFieldName()
                 , capitalize(jsonField.getFieldName())
                , jsonField.getFieldType().toString());
    }
}
