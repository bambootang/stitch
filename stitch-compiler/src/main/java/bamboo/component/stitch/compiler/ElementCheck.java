package bamboo.component.stitch.compiler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

/**
 * Created by tangshuai on 2018/3/27.
 */

public class ElementCheck {


    static void checkNoneModifiers(TypeElement element, Modifier modifier) {
        if (element.getModifiers().contains(modifier)) {
            StringBuilder sb = new StringBuilder("@Component error : ");
            sb.append(element.getQualifiedName());
            sb.append(".class can not be ");
            sb.append(modifier.name());
            sb.append(";");
            throw new IllegalStateException(sb.toString());
        }
    }

    static void checkModifiers(TypeElement element, Modifier modifier) {
        if (!element.getModifiers().contains(modifier)) {
            StringBuilder sb = new StringBuilder("@Intercept error : ");
            sb.append(element.getQualifiedName());
            sb.append(".class method must be ");
            sb.append(modifier.name());
            sb.append(";");
            throw new IllegalStateException(sb.toString());
        }
    }


    static void checkModifiers(ExecutableElement element, Modifier modifier) {
        if (!element.getModifiers().contains(modifier)) {
            StringBuilder sb = new StringBuilder("@Intercept error : ");
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            sb.append(typeElement.getQualifiedName());
            sb.append("#");
            sb.append(element.toString());
            sb.append(" method must be ");
            sb.append(modifier.name());
            sb.append(";");
            throw new IllegalStateException(sb.toString());
        }
    }

    static void checkParamerSize(ExecutableElement element, int size) {
        if (element.getParameters().size() != size) {
            StringBuilder sb = new StringBuilder("@Intercept error : ");
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            sb.append(typeElement.getQualifiedName());
            sb.append("#");
            sb.append(element.toString());
            if (element.getParameters().size() < size) {
                sb.append(" method's parameters size must be ");
                sb.append(size);
            } else {
                sb.append(" method just can has ");
                sb.append(size);
                sb.append(" parameter;");
            }
            sb.append(";");
            throw new IllegalStateException(sb.toString());
        }
    }

    static void checkExtendsActivityPage(ProcessingEnvironment environment, ExecutableElement element) {
        boolean isSuper = isSuperClass(environment, element.getParameters().get(0).asType().toString(), "bamboo.component.page.ActivityPage");
        if (!isSuper) {
            StringBuilder sb = new StringBuilder("@Intercept error : ");
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            sb.append(typeElement.getQualifiedName());
            sb.append("#");
            sb.append(element.toString());
            sb.append(" parameter must be extends from bamboo.component.page.ActivityPage;");
            throw new IllegalStateException(sb.toString());
        }
    }


    static void checkExtendsActivity(ProcessingEnvironment environment, TypeElement element) {
        boolean isSuper = isSuperClass(environment, element.getQualifiedName().toString(), "android.app.Activity");
        if (!isSuper) {
            StringBuilder sb = new StringBuilder("@Exported error : ");
            sb.append(element.getQualifiedName());
            sb.append(" must be extends from android.app.Activity;");
            throw new IllegalStateException(sb.toString());
        }
    }

    static void checkExtendsComponentLife(ProcessingEnvironment environment, TypeElement element) {
        boolean isSuper = isSuperClass(environment, element.getQualifiedName().toString(), "bamboo.component.lifecycle.ComponentLife");
        if (!isSuper) {
            StringBuilder sb = new StringBuilder("@Component error : ");
            sb.append(element.getQualifiedName());
            sb.append(" must be extends from bamboo.component.lifecycle.ComponentLife;");
            throw new IllegalStateException(sb.toString());
        }
    }

    static void checkExtendsActivityPage(ProcessingEnvironment environment, TypeElement element, TypeElement valueElement) {
        boolean isSuper = isSuperClass(environment, valueElement.getQualifiedName().toString(), "bamboo.component.page.ActivityPage");
        if (!isSuper) {
            StringBuilder sb = new StringBuilder("@Exported error : ");
            sb.append("at ");
            sb.append(element.getQualifiedName());
            sb.append(" @Exported's value ");
            sb.append(valueElement.getQualifiedName());
            sb.append(" must be extends from bamboo.component.page.ActivityPage;");
            throw new IllegalStateException(sb.toString());
        }
    }


    private static boolean isSuperClass(ProcessingEnvironment environment, String element, String superElement) {
        TypeElement variableType = environment.getElementUtils().getTypeElement(element);
        TypeElement superType = environment.getElementUtils().getTypeElement(superElement);
        if (variableType.getQualifiedName().equals(superType.getQualifiedName())) {
            return true;
        } else if (variableType != environment.getElementUtils().getTypeElement("java.lang.Object")) {
            return isSuperClass(environment, variableType.getSuperclass().toString(), superElement);
        } else {
            return false;
        }
    }

    private static boolean isInterfaceImp(ProcessingEnvironment environment, String element, String superElement) {
        TypeElement variableType = environment.getElementUtils().getTypeElement(element);
        TypeElement superType = environment.getElementUtils().getTypeElement(superElement);
        if (variableType.getQualifiedName().equals(superType.getQualifiedName())) {
            return true;
        } else if (variableType != environment.getElementUtils().getTypeElement("java.lang.Object")) {
            return isSuperClass(environment, variableType.getInterfaces().toString(), superElement);
        } else {
            return false;
        }
    }

    public static void checkHasEmptyConstructor(TypeElement typeElement) {
        List<? extends Element> elementList = typeElement.getEnclosedElements();
        boolean hasDefineConstructor = false;
        for (Element element : elementList) {
            if (element.getKind() == ElementKind.CONSTRUCTOR) {
                hasDefineConstructor = true;
                ExecutableElement executableElement = (ExecutableElement) element;
                if (executableElement.getParameters().size() > 0) {
                    continue;
                } else {
                    checkModifiers(executableElement, Modifier.PUBLIC);
                    return;
                }
            }
        }
        if (hasDefineConstructor) {
            throw new IllegalStateException("@Service error: " + typeElement.getQualifiedName() + " must has a default empty constructor;");
        }
    }

    public static void checkServiceInterfaceMatch(ProcessingEnvironment processingEnv, TypeElement element, List classes) {
        if (classes == null || classes.size() == 0) {
            return;
        }
        TypeElement typeElement = element;
        List<String> interfaces = new ArrayList<>();
        List<? extends TypeMirror> typeMirrors = typeElement.getInterfaces();
        for (TypeMirror mirror : typeMirrors) {
            interfaces.add(mirror.toString() + ".class");
        }
        while (!typeElement.getSuperclass().toString().equals("java.lang.Object")) {
            typeElement = processingEnv.getElementUtils().getTypeElement(typeElement.getSuperclass().toString());
            List<? extends TypeMirror> tmpTypeMirrors = typeElement.getInterfaces();
            for (TypeMirror mirror : tmpTypeMirrors) {
                interfaces.add(mirror.toString() + ".class");
            }
        }
        for (Object o : classes) {
            if (!interfaces.contains(o.toString())) {
                throw new IllegalStateException("@Service error: " + element.getQualifiedName() + " interface not match all;");
            }
        }

    }
}
