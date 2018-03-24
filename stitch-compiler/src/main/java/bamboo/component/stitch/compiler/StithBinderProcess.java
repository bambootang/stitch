package bamboo.component.stitch.compiler;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableList;

import java.io.IOException;
import java.io.Writer;
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
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.JavaFileObject;
import javax.tools.StandardLocation;

import bamboo.component.stitch.anno.Exported;
import bamboo.component.stitch.anno.Component;
import bamboo.component.stitch.anno.Service;


/**
 * Created by tangshuai on 2018/3/19.
 */

@AutoService(Processor.class)
public final class StithBinderProcess extends AbstractProcessor {


    bamboo.component.stitch.compiler.ComponentBinding binding = new bamboo.component.stitch.compiler.ComponentBinding();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Set<? extends Element> lifeElements = roundEnv.getElementsAnnotatedWith(Component.class);
        processLifeCycle(lifeElements);
        Set<? extends Element> serviceElements = roundEnv.getElementsAnnotatedWith(Service.class);
        processServiceAnnotaion(serviceElements);
        Set<? extends Element> pageElements = roundEnv.getElementsAnnotatedWith(Exported.class);
        processPageAnnotation(pageElements);
        if (lifeElements.size() != 0 || serviceElements.size() != 0 || pageElements.size() != 0)
            writeComponentBinding();
        return true;
    }

    private void writeComponentBinding() {
        try {
            if (binding.componentImpPackage == null || binding.componentImpPackage.length() == 0) {
                if (binding.serviceBindings != null && binding.serviceBindings.size() > 0) {
                    String implement = binding.serviceBindings.get(0).implementClass;
                    binding.componentImpPackage = implement.substring(0, implement.lastIndexOf("."));
                    binding.componentName = "";
                } else if (binding.activityBindings != null && binding.activityBindings.size() > 0) {
                    String implement = binding.activityBindings.get(0).targetActivity;
                    binding.componentImpPackage = implement.substring(0, implement.lastIndexOf("."));
                    binding.componentName = "";
                } else {
                    return;
                }
            }
            ModuleSovle moduleSovle = new ModuleSovle(processingEnv.getFiler()
                    .createResource(StandardLocation.SOURCE_OUTPUT, "", "tmp"), "tmp");
            String packageName = moduleSovle.findPackageName();
            String moduleName = moduleSovle.getModuleName();
            if (packageName != null && moduleName != null) {
                binding.modulePackageId = packageName;
                binding.moduleName = moduleName;
            } else {
                binding.modulePackageId = binding.componentImpPackage;
                binding.moduleName = binding.componentName + "_ComponentBinding";
            }
            JavaFileObject javaFileObject = processingEnv.getFiler()
                    .createSourceFile(binding.getBindingClassName());

            Writer writer = javaFileObject.openWriter();
            writer.write(binding.getPackageCode());
            writer.write(binding.getImportCode());
            writer.write(binding.getClassStartCode());
            writer.write(binding.getBindingCode());
            writer.write(binding.getClassCloseCode());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(Component.class.getCanonicalName());
        types.add(Exported.class.getCanonicalName());
        types.add(Service.class.getCanonicalName());
        return types;
    }

    private void processPageAnnotation(Set<? extends Element> pageElements) {
        ImmutableList.Builder<ActivityBinding> pageBindingBuilder = ImmutableList.builder();
        for (Element e : pageElements) {
            TypeElement element = (TypeElement) e;
            Object linkClass = getAutoLink(element);
            ActivityBinding activityBinding = new ActivityBinding();
            activityBinding.pageLink = linkClass.toString();
            activityBinding.targetActivity = element.getQualifiedName().toString();
            pageBindingBuilder.add(activityBinding);
        }
        binding.activityBindings = pageBindingBuilder.build();
    }


    private void processLifeCycle(Set<? extends Element> elements) {
        if (elements.size() > 1) {
            throw new IllegalArgumentException("duplicate Component.");
        }
        for (Element e : elements) {
            binding.componentImpPackage = "" + e.getEnclosingElement();
            binding.componentName = e.getSimpleName().toString();
        }
    }


    private void processServiceAnnotaion(Set<? extends Element> elements) {
        ImmutableList.Builder<ServiceBinding> serviceBindings = ImmutableList.builder();
        for (Element e : elements) {
            TypeElement typeElement = (TypeElement) e;
            List classes = getInterfaces(typeElement);
            if (classes != null) {
                for (Object o : classes) {
                    ServiceBinding serviceBinding = new ServiceBinding();
                    String oClassName = o.toString().substring(0, o.toString().length() - 6);
                    serviceBinding.interfaceClass = oClassName;
                    serviceBinding.implementClass = typeElement.getQualifiedName().toString();
                    serviceBindings.add(serviceBinding);
                }
            } else {
                List<TypeMirror> interfaces = (List<TypeMirror>) typeElement.getInterfaces();
                for (TypeMirror typeMirror : interfaces) {
                    ServiceBinding serviceBinding = new ServiceBinding();
                    String oClassName = typeMirror.toString();
                    serviceBinding.interfaceClass = oClassName;
                    serviceBinding.implementClass = typeElement.getQualifiedName().toString();
                    serviceBindings.add(serviceBinding);
                }
            }
        }
        binding.serviceBindings = serviceBindings.build();
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


    public List getInterfaces(TypeElement foo) {
        AnnotationMirror am = getAnnotationMirror(foo, Service.class);
        if (am == null) {
            return null;
        }
        AnnotationValue av = getAnnotationValue(am, "value");
        if (av == null) {
            return null;
        } else {
            return (List) av.getValue();
        }
    }


    public Object getAutoLink(TypeElement foo) {
        AnnotationMirror am = getAnnotationMirror(foo, Exported.class);
        if (am == null) {
            return null;
        }
        AnnotationValue av = getAnnotationValue(am, "value");
        if (av == null) {
            return null;
        } else {
            return av.getValue();
        }
    }
}
