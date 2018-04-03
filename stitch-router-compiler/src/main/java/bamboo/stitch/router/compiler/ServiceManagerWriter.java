package bamboo.stitch.router.compiler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by tangshuai on 2018/4/1.
 */

public class ServiceManagerWriter {

    private static final String TAB_SPACE = "    ";
    private static final String TAB_SPACE_DOUBLE = TAB_SPACE + TAB_SPACE;
    private static final String TAB_SPACE_TREBLE = TAB_SPACE + TAB_SPACE + TAB_SPACE;

    Writer writer;
    List<ServiceMethodBinding> serviceMethodBindings;

    public ServiceManagerWriter(Writer writer, List<ServiceMethodBinding> serviceMethodBindingList) {
        this.writer = writer;
        this.serviceMethodBindings = serviceMethodBindingList;
    }

    public void write() throws IOException {

        for (ServiceMethodBinding serviceMethodBinding : serviceMethodBindings) {

            String methodName = (serviceMethodBinding.aliasName == null ? serviceMethodBinding.methodName : serviceMethodBinding.aliasName);
            String returnType = serviceMethodBinding.returnType;
            //如果有返回值，给出defaultValue参数
            if (!serviceMethodBinding.defaultValue.equals("")) {

                writer.write(TAB_SPACE + "public static " + returnType + " " + methodName + "(");
                for (int i = 0; i < serviceMethodBinding.parametersType.length; i++) {
                    String parameter = serviceMethodBinding.parametersType[i] + " " + serviceMethodBinding.parametersName[i];
                    writer.write(parameter);
                    if (i < serviceMethodBinding.parametersType.length - 1) {
                        writer.write(", ");
                    }
                }
                writer.write(") {\n");
                if (!serviceMethodBinding.defaultValue.equals("")) {
                    writer.write(TAB_SPACE_DOUBLE + "return ");
                } else {
                    writer.write(TAB_SPACE_DOUBLE);
                }

                writer.write(methodName + "(");
                for (int i = 0; i < serviceMethodBinding.parametersName.length; i++) {
                    String parameter = serviceMethodBinding.parametersName[i];
                    writer.write(parameter);
                    if (i < serviceMethodBinding.parametersType.length - 1) {
                        writer.write(", ");
                    }
                }
                if (!serviceMethodBinding.defaultValue.equals("")) {
                    if (serviceMethodBinding.parametersType.length > 0) {
                        writer.write(", ");
                    }
                    writer.write(serviceMethodBinding.defaultValue);
                }
                writer.write(");\n");
                writer.write(TAB_SPACE + "}\n\n");
            }

            writer.write(TAB_SPACE + "public static " + serviceMethodBinding.returnType + " " + methodName + "(");
            for (int i = 0; i < serviceMethodBinding.parametersType.length; i++) {
                String parameter = serviceMethodBinding.parametersType[i] + " " + serviceMethodBinding.parametersName[i];
                writer.write(parameter);
                if (i < serviceMethodBinding.parametersType.length - 1) {
                    writer.write(", ");
                }
            }
            if (!serviceMethodBinding.defaultValue.equals("")) {
                if (serviceMethodBinding.parametersType.length > 0) {
                    writer.write(", ");
                }
                writer.write(serviceMethodBinding.returnType + " defaultValue");
            }
            writer.write(") {\n");
            writer.write(TAB_SPACE_DOUBLE + serviceMethodBinding.serviceInterfaceClass + " service = StitcherHelper.searchService(" + serviceMethodBinding.serviceInterfaceClass + ".class);\n");
            writer.write(TAB_SPACE_DOUBLE + "if(service != null) {\n");
            if (serviceMethodBinding.defaultValue != null && !serviceMethodBinding.defaultValue.equals("")) {
                writer.write(TAB_SPACE_TREBLE + "return ");
                writer.write("service." + serviceMethodBinding.methodName + "(");
            } else {
                writer.write(TAB_SPACE_TREBLE + "service." + serviceMethodBinding.methodName + "(");
            }
            for (int i = 0; i < serviceMethodBinding.parametersName.length; i++) {
                String parameter = serviceMethodBinding.parametersName[i];
                writer.write(parameter);
                if (i < serviceMethodBinding.parametersName.length - 1) {
                    writer.write(", ");
                }
            }
            writer.write(");\n");
            writer.write(TAB_SPACE_DOUBLE + "}\n");
            if (serviceMethodBinding.defaultValue.equals("")) {
                writer.write(TAB_SPACE_DOUBLE + "return;\n");
            } else {
                writer.write(TAB_SPACE_DOUBLE + "return defaultValue;\n");
            }
            writer.write(TAB_SPACE + "}\n\n");
        }
    }
}
