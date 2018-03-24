package bamboo.component.stitch.compiler;

import com.google.common.collect.ImmutableList;

import java.util.HashSet;

/**
 * Created by tangshuai on 2018/3/19.
 */

public class ComponentBinding {

    private static final String REGISTRY_INTERFACE = "bamboo.component.IRegistry";

    private static final String TAB_SPACE = "    ";
    private static final String TAB_SPACE_DOUBLE = TAB_SPACE + TAB_SPACE;
    private static final String TAB_SPACE_TREBLE = TAB_SPACE + TAB_SPACE + TAB_SPACE;

    //包名
    String componentImpPackage;

    //具体的ComponentApplication实现类,不包含包名
    String componentName;

    String moduleName;

    String modulePackageId;

    ImmutableList<ServiceBinding> serviceBindings;
    ImmutableList<ActivityBinding> activityBindings;

    public String getBindingClassName() {
        return (modulePackageId == null ? componentImpPackage : modulePackageId) + "." + moduleName + "_ComponentBinding";
    }

    public String getPackageCode() {
        if (modulePackageId == null) {
            return "package " + componentImpPackage + ";\n\n";
        } else {
            return "package " + modulePackageId + ";\n\n";
        }
    }

    public String getClassStartCode() {
        String className = moduleName + "_ComponentBinding";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("public class");//modifies
        stringBuilder.append(" " + className);//className
        if (componentName != null && componentName.length() > 0) {
            stringBuilder.append(" extends " + componentImpPackage + "." + componentName);//继承Component实现类
        } else {
            stringBuilder.append(" extends bamboo.component.lifecycle.ComponentLife");//继承Component实现类
            componentImpPackage = "bamboo.component.lifecycle";
            componentName = "ComponentLife";
        }
        stringBuilder.append(" implements " + REGISTRY_INTERFACE + " {\n\n");//继承Component实现类
        return stringBuilder.toString();
    }

    public String getClassCloseCode() {
        return "}";
    }


    public String getBindingCode() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(TAB_SPACE + "public void register(ServiceRegistry serviceRegistry, ActivityRegistry activityRegistry) {\n\n");
        if (serviceBindings != null) {
            //define service object
            HashSet<String> objectName = new HashSet<>();//解决重名
            for (ServiceBinding service : serviceBindings) {
                if (objectName.contains(service.implementClass)) {
                    continue;
                }
                objectName.add(service.implementClass);
                stringBuilder.append(TAB_SPACE_DOUBLE + service.implementClass);
                stringBuilder.append(" ");
                stringBuilder.append(service.implementClass.replace(".", "_").toLowerCase());
                stringBuilder.append(" = ");
                stringBuilder.append(String.format("new %s();\n", service.implementClass));
            }
            for (ServiceBinding service : serviceBindings) {
                stringBuilder.append(String.format(TAB_SPACE_DOUBLE + "serviceRegistry.register(%s, %s);\n\n"
                        , service.interfaceClass + ".class"
                        , service.implementClass.replace(".", "_").toLowerCase()));
            }
        }
        if (activityBindings != null) {
            for (ActivityBinding page : activityBindings) {
                stringBuilder.append(String.format(TAB_SPACE_DOUBLE + "activityRegistry.register(\"%s\",\"%s\");\n\n"
                        , page.pageLink
                        , page.targetActivity));
            }
        }

        stringBuilder.append(TAB_SPACE + "}\n\n");
        getGetNameCode(stringBuilder);
        return stringBuilder.toString();
    }

    private String getGetNameCode(StringBuilder stringBuilder) {
        //重写getName方法
        stringBuilder.append(TAB_SPACE + "public String getName() {\n");
        stringBuilder.append(TAB_SPACE_DOUBLE + "return \"" + componentImpPackage + "." + componentName + "\";\n");
        stringBuilder.append(TAB_SPACE + "}\n");
        return null;
    }

    public String getImportCode() {
        return "import bamboo.component.service.ServiceRegistry;\n" +
                "import bamboo.component.page.ActivityRegistry;\n\n";
    }

}
