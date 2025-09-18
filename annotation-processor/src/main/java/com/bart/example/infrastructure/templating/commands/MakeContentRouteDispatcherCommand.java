package com.bart.example.infrastructure.templating.commands;

import com.bart.example.infrastructure.templating.commands.model.DispatchClassesCommandModel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.stream.Collectors;

import static java.text.MessageFormat.format;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MakeContentRouteDispatcherCommand {
    @Getter
    private final static MakeContentRouteDispatcherCommand instance = new MakeContentRouteDispatcherCommand();

    private static final String ROUTE_DISPATCHER_TEMPLATE = """
       package {0};
       
       import com.bart.example.infrastructure.webserving.handlers.RouteDispatcher;
       import com.bart.example.infrastructure.webserving.handlers.AbstractContextHandler;
       import com.bart.example.infrastructure.webserving.commands.*;
       import static com.bart.example.infrastructure.webserving.services.PathComparatorUtil.*;
       import java.util.Optional;
 
       public class {1} extends RouteDispatcher '{'
          @Override
          public Optional<AbstractContextHandler> getHandler(String urlPath) '{'
              String path = removeTrailingSlash(urlPath);
              int pathHash = path.hashCode();
                 switch (pathHash) '{'
       {2}
                    default:
                       {3}
                       return Optional.empty();
              '}'
          '}'
       '}'
       """;

    private static String DIRECT_SWITCH = """
                   case {0}: return Optional.of({1}.getInstance());
            """;

    private static String ADVANCED_COMPARE = """
                   if (pathsMatch("{0}",path)) return Optional.of({1}.getInstance());
            """;

    public String execute(DispatchClassesCommandModel dispatchClassesCommandModel) {
        String directMatch = dispatchClassesCommandModel.getShortPaths()
                .stream()
                .map(path -> format(DIRECT_SWITCH, String.valueOf(path.getPathHashcode()), path.getTargetClass()))
                .collect(Collectors.joining());
        String indirectMatch = dispatchClassesCommandModel.getFullPaths()
                .stream()
                .map(path -> format(ADVANCED_COMPARE,path.getPathName(), path.getTargetClass()))
                .collect(Collectors.joining());
        return format(ROUTE_DISPATCHER_TEMPLATE
                ,dispatchClassesCommandModel.getPackageName()
                ,dispatchClassesCommandModel.getClassName()
                ,directMatch,indirectMatch);
    }
}
