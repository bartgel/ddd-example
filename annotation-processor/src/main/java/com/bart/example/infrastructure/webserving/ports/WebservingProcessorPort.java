package com.bart.example.infrastructure.webserving.ports;

import com.bart.example.infrastructure.webserving.usecases.ProcessAllAnnotationUseCase;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.tools.Diagnostic;
import java.util.*;

@SupportedAnnotationTypes({
        "com.bart.example.infrastructure.webserving.annotations.GetMapping",
        "com.bart.example.infrastructure.webserving.annotations.PostMapping",
        "com.bart.example.infrastructure.webserving.annotations.PutMapping",
        "com.bart.example.infrastructure.webserving.annotations.DeleteMapping",
        "com.bart.example.infrastructure.webserving.annotations.PatchMapping"})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
public class WebservingProcessorPort extends AbstractProcessor {
    private Filer filer;
    private Messager messager;
    private ProcessAllAnnotationUseCase processAllAnnotationUseCase = ProcessAllAnnotationUseCase.getInstance();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.filer = processingEnv.getFiler();
        this.messager = processingEnv.getMessager();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        messager.printMessage(Diagnostic.Kind.NOTE, "🔍 Processing round with annotations: " + annotations.size());
        if (roundEnv.processingOver() || annotations.isEmpty()) {
            messager.printMessage(Diagnostic.Kind.NOTE, "X Nothing to see here ");
            return false;
        }

        processAllAnnotationUseCase.execute(annotations, roundEnv,filer);


        return true;
    }

}
