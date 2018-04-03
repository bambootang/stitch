package bamboo.stitch.router.compiler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

import bamboo.stitch.router.anno.Parameter;

/**
 * Created by tangshuai on 2018/4/1.
 */

public class ActivityPageProcess {

    private TypeElement element;

    private ProcessingEnvironment environment;

    private ActivityPageBinding activityPageBinding;

    private Map<String, ParameterBinding> parameters = new HashMap<>();

    public ActivityPageProcess(ProcessingEnvironment environment, TypeElement element) {
        this.environment = environment;
        this.element = element;
        activityPageBinding = new ActivityPageBinding();
        activityPageBinding.activityPageClass = element.getQualifiedName().toString();
        activityPageBinding.activityPageName = element.getSimpleName().toString();
        activityPageBinding.imports.put(activityPageBinding.activityPageClass, activityPageBinding.activityPageName);
    }

    public ActivityPageBinding processElement() {
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        processParameterField(enclosedElements);
        processParameterMethod(enclosedElements);
        processConstruct(enclosedElements);
        activityPageBinding.parameters.addAll(parameters.values());
        return activityPageBinding;
    }

    private void processParameterField(List<? extends Element> enclosedElements) {

        for (Element e : enclosedElements) {
            ParameterBinding parameterBinding = new ParameterBinding();
            parameterBinding.name = e.getSimpleName().toString();
            Parameter parameter = e.getAnnotation(Parameter.class);
            if (parameter != null) {
                ParameterChecks.checkModifier(e, Modifier.PUBLIC);
                ParameterChecks.checkNoneModifier(e, Modifier.STATIC);
                ParameterChecks.checkNoneModifier(e, Modifier.FINAL);
                ParameterChecks.checkNoneModifier(e, Modifier.ABSTRACT);
                if (e.getKind() == ElementKind.FIELD) {
                    String value = parameter.value().trim().equals("") ? e.getSimpleName().toString() : parameter.value();
                    VariableElement variableElement = (VariableElement) e;
                    parameterBinding.name = value;
                    parameterBinding.type = 0;
                    parameterBinding.methodName = "set" + Name.toUpperStart(parameterBinding.name);
                    String type = variableElement.asType().toString();
                    if (!activityPageBinding.imports.containsKey(type)) {
                        TypeElement el = environment.getElementUtils().getTypeElement(type);
                        if (el != null) {
                            activityPageBinding.imports.put(type, el.getSimpleName().toString());
                        }
                    }
                    if (activityPageBinding.imports.containsKey(type)) {
                        type = activityPageBinding.imports.get(type);
                    }
                    parameterBinding.paramerTypes = new String[]{type};
                    parameterBinding.paramerNames = new String[]{value};
                    parameters.put(parameterBinding.name, parameterBinding);
                }
            }
        }
    }

    private void processParameterMethod(List<? extends Element> enclosedElements) {

        for (Element e : enclosedElements) {
            Parameter parameter = e.getAnnotation(Parameter.class);
            if (parameter != null) {
                ParameterChecks.checkModifier(e, Modifier.PUBLIC);
                ParameterChecks.checkNoneModifier(e, Modifier.STATIC);
                ParameterChecks.checkNoneModifier(e, Modifier.FINAL);
                ParameterChecks.checkNoneModifier(e, Modifier.ABSTRACT);
                if (e.getKind() == ElementKind.METHOD) {
                    if (parameter.value().trim().equals("")) {
                        throw new IllegalArgumentException("value must not be empty when @Parameter use for Method.");
                    }
                    ExecutableElement variableElement = (ExecutableElement) e;
                    ParameterBinding parameterBinding;
                    if (parameters.containsKey(variableElement.getSimpleName().toString())) {
                        parameterBinding = parameters.get(variableElement.getSimpleName().toString());
                    } else {
                        parameterBinding = new ParameterBinding();
                    }
                    parameterBinding.name = parameter.value();
                    parameterBinding.type = 1;
                    parameterBinding.methodName = variableElement.getSimpleName().toString();

                    parameterBinding.paramerTypes = new String[variableElement.getParameters().size()];
                    parameterBinding.paramerNames = new String[variableElement.getParameters().size()];
                    for (int i = 0; i < variableElement.getParameters().size(); i++) {
                        VariableElement p = variableElement.getParameters().get(i);
                        String type = p.asType().toString();
                        if (!activityPageBinding.imports.containsKey(type)) {
                            TypeElement el = environment.getElementUtils().getTypeElement(type);
                            if (el != null) {
                                activityPageBinding.imports.put(type, el.getSimpleName().toString());
                            }
                        }
                        if (activityPageBinding.imports.containsKey(type)) {
                            type = activityPageBinding.imports.get(type);
                        }
                        parameterBinding.paramerTypes[i] = type;
                        parameterBinding.paramerNames[i] = p.getSimpleName().toString();
                    }
                    parameters.put(parameterBinding.name, parameterBinding);
                }
            }
        }
    }

    private void processConstruct(List<? extends Element> enclosedElements) {
        for (Element e : enclosedElements) {
            if (e.getKind() == ElementKind.CONSTRUCTOR && e.getModifiers().contains(Modifier.PUBLIC)) {
                ExecutableElement executableElement = (ExecutableElement) e;
                ConstructorBinding constructorBinding = new ConstructorBinding();
                constructorBinding.className = activityPageBinding.activityPageClass;
                int size = executableElement.getParameters().size();
                constructorBinding.parametersType = new String[size];
                constructorBinding.parametersName = new String[size];
                for (int i = 0; i < size; i++) {
                    VariableElement p = executableElement.getParameters().get(i);
                    String type = p.asType().toString();
                    if (!activityPageBinding.imports.containsKey(type)) {
                        TypeElement el = environment.getElementUtils().getTypeElement(type);
                        if (el != null) {
                            activityPageBinding.imports.put(type, el.getSimpleName().toString());
                        }
                    }
                    if (activityPageBinding.imports.containsKey(type)) {
                        type = activityPageBinding.imports.get(type);
                    }
                    constructorBinding.parametersType[i] = type;
                    constructorBinding.parametersName[i] = p.getSimpleName().toString();
                }
                activityPageBinding.constructors.add(constructorBinding);
            }
        }
        if (activityPageBinding.constructors.size() == 0) {
            throw new IllegalStateException(element.getQualifiedName() + " need at least 1 PUBLIC Constructor.");
        }
    }
}
