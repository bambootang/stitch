package bamboo.stitch.router.compiler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

/**
 * Created by tangshuai on 2018/4/1.
 */

public class ActivityPageWriter {

    private static final String TAB_SPACE = "    ";
    private static final String TAB_SPACE_DOUBLE = TAB_SPACE + TAB_SPACE;
    private static final String TAB_SPACE_TREBLE = TAB_SPACE + TAB_SPACE + TAB_SPACE;

    Writer writer;
    List<ActivityPageBinding> activityPageBindingList;

    public ActivityPageWriter(Writer writer, List<ActivityPageBinding> activityPageBindingList) {
        this.writer = writer;
        this.activityPageBindingList = activityPageBindingList;
    }

    public void write() throws IOException {

        writerNewInstanceMethod();
        writerNewDelegateClass();
    }

    private void writerNewDelegateClass() throws IOException {
        for (ActivityPageBinding pageBinding : activityPageBindingList) {
            String delegateName = pageBinding.activityPageName + "Delegate";
            //class define
            writer.write(TAB_SPACE + "public static class ");
            writer.write(delegateName + " extends ActivityPageDelegate<" + pageBinding.activityPageName + "> {\n\n");

            //constructor define
            writer.write(TAB_SPACE_DOUBLE + "public " + delegateName + "(");
            writer.write(pageBinding.activityPageName);
            writer.write(" activityPage");
            writer.write(") {\n");
            writer.write(TAB_SPACE_TREBLE + "super(activityPage);\n");
            writer.write(TAB_SPACE_DOUBLE + "}\n\n");

            //field <set> method define
            StringBuilder stringBuffer = new StringBuilder();
            for (ParameterBinding binding : pageBinding.parameters) {
                //method return
                stringBuffer.append(TAB_SPACE_DOUBLE + "public ")
                        .append(delegateName)
                        .append(" ");
                //method name
                stringBuffer.append(binding.methodName);
                stringBuffer.append("(");
                //method parameters
                for (int i = 0; i < binding.paramerTypes.length; i++) {
                    stringBuffer.append(binding.paramerTypes[i]);
                    stringBuffer.append(" ");
                    stringBuffer.append(binding.paramerNames[i]);
                    if (i < binding.paramerTypes.length - 1) {
                        stringBuffer.append(", ");
                    }
                }
                stringBuffer.append(") {\n");
                if (binding.type == 0) {
                    stringBuffer.append(TAB_SPACE_TREBLE + "activityPage.").append(binding.name)
                            .append(" = ")
                            .append(binding.name).append(";\n");
                } else {
                    stringBuffer.append(TAB_SPACE_TREBLE + "activityPage.").append(binding.methodName).append("(");
                    for (int i = 0; i < binding.paramerNames.length; i++) {
                        stringBuffer.append(binding.paramerNames[i]);
                        if (i < binding.paramerNames.length - 1) {
                            stringBuffer.append(", ");
                        }
                    }
                    stringBuffer.append(");\n");
                }
                stringBuffer.append(TAB_SPACE_TREBLE + "return this;\n");
                stringBuffer.append(TAB_SPACE_DOUBLE + "}\n\n");
            }
            writer.write(stringBuffer.toString());
            writer.write(TAB_SPACE + "}\n\n");
        }
    }

    private void writerNewInstanceMethod() throws IOException {

        for (ActivityPageBinding pageBinding : activityPageBindingList) {

            for (ConstructorBinding constructorBinding : pageBinding.constructors) {
                writer.write(TAB_SPACE + "public static " + pageBinding.activityPageName + "Delegate new" + Name.toUpperStart(pageBinding.activityPageName));
                writer.write("(");

                for (int i = 0; i < constructorBinding.parametersType.length; i++) {
                    writer.write(constructorBinding.parametersType[i]);
                    writer.write(" ");
                    writer.write(constructorBinding.parametersName[i]);
                    if (i < constructorBinding.parametersType.length - 1) {
                        writer.write(", ");
                    }
                }
                writer.write(") {\n");
                writer.write(TAB_SPACE_DOUBLE + "return new ");
                writer.write(pageBinding.activityPageName + "Delegate(");
                writer.write("new " + pageBinding.activityPageName + "(");
                for (int i = 0; i < constructorBinding.parametersName.length; i++) {
                    writer.write(constructorBinding.parametersName[i]);
                    if (i < constructorBinding.parametersName.length - 1) {
                        writer.write(", ");
                    }
                }
                writer.write("));\n");
                writer.write(TAB_SPACE + "}\n\n");
            }
        }
    }
}
