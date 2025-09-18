package com.bart.example.infrastructure.injector.port;

import com.bart.example.infrastructure.injector.usecases.GenerateDependenciesUseCase;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedAnnotationTypes({
        "com.bart.example.infrastructure.injector.annotations.Inject",
        "com.bart.example.infrastructure.injector.annotations.Singleton",
        "com.bart.example.infrastructure.injector.annotations.Bean"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class InjectorProcessorPort extends AbstractProcessor {
    private final static GenerateDependenciesUseCase generateDependenciesUseCase
            = GenerateDependenciesUseCase.getInstance();

    private Filer filer;
    private Messager messager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "init --- Start Processing Injectors");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "Start Processing Injectors");
        if (roundEnv.processingOver() || annotations.isEmpty()) {
            return false;
        }
        messager.printMessage(Diagnostic.Kind.NOTE, "Valid Injectors");

        try {
            // Find all @Singleton classes
            return generateDependenciesUseCase.execute(roundEnv, filer);
        } catch (Exception e) {
            error("Error in DI processing: " + e.getMessage());
            e.printStackTrace();
        }
        return true;
    }

    private void error(String message) {
        messager.printMessage(Diagnostic.Kind.ERROR, message);
    }
}
