package org.deustomed.SQLFramework;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("org.deustomed.SQLFramework.SQLField")
public class AnnotationRuleEnforcer extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(SQLField.class)) {
            TypeElement classElement = (TypeElement) annotatedElement.getEnclosingElement();
            if (classElement.getAnnotation(SQLTable.class) == null) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                        "Classes with fields annotated with @SQLField must also be annotated with @SQLTable",
                        classElement);
            }
        }
        return true;
    }
}
