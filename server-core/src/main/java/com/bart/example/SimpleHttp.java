package com.bart.example;

import com.bart.example.infrastructure.webserving.ports.LightweightServer;
import com.bart.example.infrastructure.injector.ports.CompileTimeDIContainer;

public class SimpleHttp {
    public static void main(String[] args) throws Exception {
        LightweightServer lightweightServer =
                new LightweightServer(8080,20, CompileTimeDIContainer.getInternalRouteDispatcher());
        lightweightServer.start();
    }
}
