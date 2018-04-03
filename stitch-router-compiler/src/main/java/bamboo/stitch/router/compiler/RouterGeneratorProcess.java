package bamboo.stitch.router.compiler;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Types;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import bamboo.stitch.router.anno.Alias;
import bamboo.stitch.router.anno.Wrapper;


/**
 * Created by tangshuai on 2018/3/19.
 */

@AutoService(Processor.class)
public final class RouterGeneratorProcess extends AbstractProcessor {

    private static final String TAB_SPACE = "    ";
    private static final String TAB_SPACE_DOUBLE = TAB_SPACE + TAB_SPACE;
    private static final String TAB_SPACE_TREBLE = TAB_SPACE + TAB_SPACE + TAB_SPACE;

    List<ServiceMethodBinding> serviceMethodBindingList = new ArrayList<>();

    List<ActivityPageBinding> activityPageBindingList = new ArrayList<>();

    Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        types = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> delegateElements = roundEnv.getElementsAnnotatedWith(Wrapper.class);
        processDelegate(delegateElements);
        return true;
    }

    private void processDelegate(Set<? extends Element> delegateElements) {
        for (Element element : delegateElements) {
            if (element.getKind() == ElementKind.INTERFACE) {
                processDelegateInterface((TypeElement) element);
            } else {
                ActivityPageProcess activityPageProcess = new ActivityPageProcess(processingEnv, (TypeElement) element);
                activityPageBindingList.add(activityPageProcess.processElement());
            }
        }

        try {
            if (activityPageBindingList.size() > 0) {
                writeActivityPageManager();
                activityPageBindingList.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (serviceMethodBindingList.size() > 0) {
                writeServicesManager();
                serviceMethodBindingList.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeActivityPageManager() throws IOException {
        ModuleSovle moduleSovle = new ModuleSovle(processingEnv.getFiler()
                .createResource(StandardLocation.SOURCE_OUTPUT, "", "activitypage"), "activitypage");
        String packageName = moduleSovle.findPackageName();
        JavaFileObject serviceManager = processingEnv.getFiler().createSourceFile(packageName + ".ActivityPageManager");
        Writer writer = serviceManager.openWriter();
        writer.write("package " + packageName + ";\n\n");
        Set<String> imports = new HashSet<>();
        for (ActivityPageBinding pageBinding : activityPageBindingList) {
            imports.addAll(pageBinding.imports.keySet());
        }
        imports.add("android.content.Intent");
        imports.add("bamboo.component.StitcherHelper");
        imports.add("bamboo.component.page.ActivityPage");
        //write all imports
        for (String pkg : imports) {
            //跳过基础类型的imports
            if (!(!pkg.contains(".")
                    || pkg.equals(Object.class.getCanonicalName())
                    || pkg.equals(String.class.getCanonicalName())
                    || pkg.equals(Integer.class.getCanonicalName())
                    || pkg.equals(Short.class.getCanonicalName())
                    || pkg.equals(Long.class.getCanonicalName())
                    || pkg.equals(Float.class.getCanonicalName())
                    || pkg.equals(Double.class.getCanonicalName())
                    || pkg.equals(Boolean.class.getCanonicalName())
                    || pkg.equals(Byte.class.getCanonicalName())
                    || pkg.equals(Character.class.getCanonicalName()))) {
                writer.write("import " + pkg + ";\n");
            }
        }
        writer.write("\n");
        writer.write("public class ActivityPageManager {\n\n");
        new ActivityPageWriter(writer, activityPageBindingList).write();
        writer.write(ActivityDelegate.getActivityDelegateClassCode(packageName));
        writer.write("}\n");
        writer.close();

    }

    private void writeServicesManager() throws IOException {
        ModuleSovle moduleSovle = new ModuleSovle(processingEnv.getFiler()
                .createResource(StandardLocation.SOURCE_OUTPUT, "", "service"), "service");
        String packageName = moduleSovle.findPackageName();
        JavaFileObject serviceManager = processingEnv.getFiler().createSourceFile(packageName + ".ServiceManager");
        Writer writer = serviceManager.openWriter();
        writer.write("package " + packageName + ";\n\n");
        writer.write("import bamboo.component.StitcherHelper;\n\n");
        writer.write("public class ServiceManager {\n\n");
        new ServiceManagerWriter(writer, serviceMethodBindingList).write();
        writer.write(TAB_SPACE + "}\n");
        writer.close();
    }


    private void processDelegateInterface(TypeElement element) {
        List<? extends Element> enclosedElements = element.getEnclosedElements();
        for (Element e : enclosedElements) {
            if (e.getKind() == ElementKind.METHOD) {
                ExecutableElement method = (ExecutableElement) e;
                ServiceMethodBinding serviceMethodBinding = new ServiceMethodBinding();
                serviceMethodBinding.methodName = e.getSimpleName().toString();
                Alias alias = e.getAnnotation(Alias.class);
                if (alias != null) {
                    serviceMethodBinding.aliasName = alias.value();
                }
                serviceMethodBinding.serviceInterfaceClass = element.getQualifiedName().toString();
                serviceMethodBinding.returnType = method.getReturnType().toString();
                serviceMethodBinding.parametersName = new String[method.getParameters().size()];
                serviceMethodBinding.parametersType = new String[method.getParameters().size()];
                for (int i = 0; i < serviceMethodBinding.parametersType.length; i++) {
                    VariableElement parameter = method.getParameters().get(i);
                    serviceMethodBinding.parametersType[i] = parameter.asType().toString();
                    serviceMethodBinding.parametersName[i] = parameter.getSimpleName().toString();
                }
                serviceMethodBindingList.add(serviceMethodBinding);
                if (method.getReturnType().toString().equals("int")) {
                    serviceMethodBinding.defaultValue = "Integer.MIN_VALUE";
                } else if (method.getReturnType().toString().equals("long")) {
                    serviceMethodBinding.defaultValue = "Long.MIN_VALUE";
                } else if (method.getReturnType().toString().equals("double")) {
                    serviceMethodBinding.defaultValue = "Double.MIN_VALUE";
                } else if (method.getReturnType().toString().equals("float")) {
                    serviceMethodBinding.defaultValue = "Float.MIN_VALUE";
                } else if (method.getReturnType().toString().equals("short")) {
                    serviceMethodBinding.defaultValue = "Short.MIN_VALUE";
                } else if (method.getReturnType().toString().equals("char")) {
                    serviceMethodBinding.defaultValue = "Character.MIN_VALUE";
                } else if (method.getReturnType().toString().equals("byte")) {
                    serviceMethodBinding.defaultValue = "Byte.MIN_VALUE";
                } else if (method.getReturnType().toString().equals("boolean")) {
                    serviceMethodBinding.defaultValue = "false";
                } else if (method.getReturnType().toString().equals("void")) {
                    serviceMethodBinding.defaultValue = "";
                } else if (method.getReturnType().toString().equals(Integer.class.getCanonicalName())) {
                    serviceMethodBinding.defaultValue = "Integer.valueOf(Integer.MIN_VALUE)";
                } else if (method.getReturnType().toString().equals(Long.class.getCanonicalName())) {
                    serviceMethodBinding.defaultValue = "Long.valueOf(Long.MIN_VALUE)";
                } else if (method.getReturnType().toString().equals(Double.class.getCanonicalName())) {
                    serviceMethodBinding.defaultValue = "Double.valueOf(Double.MIN_VALUE)";
                } else if (method.getReturnType().toString().equals(Float.class.getCanonicalName())) {
                    serviceMethodBinding.defaultValue = "Float.valueOf(Float.MIN_VALUE)";
                } else if (method.getReturnType().toString().equals(Short.class.getCanonicalName())) {
                    serviceMethodBinding.defaultValue = "Short.valueOf(Short.MIN_VALUE)";
                } else if (method.getReturnType().toString().equals(Character.class.getCanonicalName())) {
                    serviceMethodBinding.defaultValue = "Character.valueOf(Character.MIN_VALUE)";
                } else if (method.getReturnType().toString().equals(Byte.class.getCanonicalName())) {
                    serviceMethodBinding.defaultValue = "Byte.valueOf(Byte.MIN_VALUE)";
                } else if (method.getReturnType().toString().equals(Boolean.class.getCanonicalName())) {
                    serviceMethodBinding.defaultValue = "Boolean.FALSE";
                }
            }
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Wrapper.class.getCanonicalName());
        return types;
    }


    private static AnnotationMirror getAnnotationMirror(TypeElement typeElement, Class<?> clazz) {
        String clazzName = clazz.getName();
        for (AnnotationMirror m : typeElement.getAnnotationMirrors()) {
            if (m.getAnnotationType().toString().equals(clazzName)) {
                return m;
            }
        }
        return null;
    }

    private static AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String key) {
        for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : annotationMirror.getElementValues().entrySet()) {
            if (entry.getKey().getSimpleName().toString().equals(key)) {
                return entry.getValue();
            }
        }
        return null;
    }

}
