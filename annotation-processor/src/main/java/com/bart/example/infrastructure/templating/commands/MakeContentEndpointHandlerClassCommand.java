package com.bart.example.infrastructure.templating.commands;

import com.bart.example.infrastructure.templating.ports.model.EndpointClass;
import com.bart.example.infrastructure.templating.ports.model.EndpointMethodToDispatch;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.util.stream.Collectors;

import static com.bart.example.infrastructure.templating.services.ClassMemberTransformationService.getSimpleClassName;
import static java.text.MessageFormat.format;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MakeContentEndpointHandlerClassCommand {
    @Getter
    private final static MakeContentEndpointHandlerClassCommand instance = new MakeContentEndpointHandlerClassCommand();

    private static final String CONTEXT_CLASS_TEMPLATE = """
        package {0};
        
        import java.io.IOException;
        import com.bart.example.infrastructure.json.ports.JsonWriter;
        import com.bart.example.infrastructure.injector.ports.CompileTimeDIContainer;
        import com.bart.example.infrastructure.webserving.handlers.RouteDispatcher;
        import com.bart.example.infrastructure.webserving.handlers.AbstractContextHandler;
        import com.sun.net.httpserver.HttpExchange;
        
        public class {1} extends AbstractContextHandler '{'
           private final static {1} INSTANCE = new {1}();
           private final JsonWriter jsonWriter = CompileTimeDIContainer.getJsonWriter();
           
           public static {1} getInstance () '{'
             return INSTANCE;
           '}'
           
           private {1}() '{'
             super();
           '}'
        {2}
        '}'
        """;

    private static final String METHOD_STRING_TEMPLATE = """
          @Override
          protected void handle{0}(HttpExchange exchange) throws IOException '{'
            String string = CompileTimeDIContainer.get{1}().{2}(exchange);
            sendResponse(exchange, {4}, string);
          '}'
        """;

    private static final String METHOD_JSON_TEMPLATE = """
          @Override
          protected void handle{0}(HttpExchange exchange) throws IOException '{'
            {3} obj = CompileTimeDIContainer.get{1}().{2}(exchange);
            String string = jsonWriter.toJson{5}(obj);
            sendResponseJson(exchange, {4}, string);
          '}'
        """;



    public String execute(EndpointClass endpointClass) {
        String methodsCode = endpointClass.getMethods().stream()
                .map(this::generateMethod)
                .collect(Collectors.joining("\n\n"));

        return format(CONTEXT_CLASS_TEMPLATE
                , endpointClass.getEndpointClasses().getPackageName()
                , endpointClass.getClassName()
                , methodsCode);

    }


    private String generateMethod(EndpointMethodToDispatch method) {
        return format(method.getReturnType().equals("java.lang.String") ? METHOD_STRING_TEMPLATE : METHOD_JSON_TEMPLATE,
                method.getMethod(),
                getSimpleClassName(method.getDispatchClassName()),
                method.getDispatchMethodName(),
                method.getReturnType(),
                method.getMethod().equals("POST") ? 201 : 200,
                method.getSubType() == null || method.getSubType().isEmpty() ? "" : "List" + getSimpleClassName(method.getSubType())
        );
    }

}
