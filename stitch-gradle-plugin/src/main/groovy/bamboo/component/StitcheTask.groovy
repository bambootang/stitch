package bamboo.component

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction

/**
 * Created by tangshuai on 2018/3/23.
 */

class StitcheTask extends DefaultTask {

    private static final String BindingFile_Suffix = "_ComponentBinding.java"

    private static final String BindingFileName = "_ComponentBinding"

    private static final META_NODE_ANDROID_VALUE_ATTRIBUTE_NAME = "android:value"

    private static final META_NODE_ANDROID_VALUE_ATTRIBUTE_VALUE = "ComponentLife"

    private static final META_NODE_ANDROID_NAME_ATTRIBUTE_NAME = "android:name"

    private static final META_NODE_NAME = "meta-data"

    private static final APPLICATION_NODE = "application"

    @InputDirectory
    File manifestFileDir

    @TaskAction
    void action() {

        File manifestFile = new File(manifestFileDir, "AndroidManifest.xml")

        XmlParser xmlParser = new XmlParser()

        Node node = xmlParser.parse(manifestFile)

        String packageId = node.attributes().get("package")

        String componentBinding = "${packageId}.${toCamlStyle(project.name)}${BindingFileName}"

        one:
        for (Node n : node.children()) {
            if (n.name() == APPLICATION_NODE) {
                for (Node childN : n.children()) {
                    if (childN.name() == META_NODE_NAME) {
                        // has added component
                        for (Map.Entry entry : childN.attributes().entrySet()) {
                            if (entry.value == componentBinding) {
                                break one
                            }
                        }
                    }
                }

                Map<String, Object> attributes = new HashMap<>()
                attributes.put(META_NODE_ANDROID_VALUE_ATTRIBUTE_NAME, META_NODE_ANDROID_VALUE_ATTRIBUTE_VALUE)
                attributes.put(META_NODE_ANDROID_NAME_ATTRIBUTE_NAME, componentBinding)
                n.appendNode(META_NODE_NAME, attributes)
                break
            }
        }


        FileWriter fileWriter = new FileWriter(manifestFile)
        XmlNodePrinter xmlNodePrinter = new XmlNodePrinter(new PrintWriter(fileWriter))
        xmlNodePrinter.print(node)
        fileWriter.close()
    }

    static String toCamlStyle(String str) {

        if (str == null || "".equals(str.trim())) {
            return ""
        }
        int length = str.length()
        boolean continuous = false
        StringBuilder sb = new StringBuilder()
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            if (('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z')) {
                sb.append(continuous ? c : (("$c").toUpperCase()))
                continuous = true
            } else {
                continuous = false
            }
        }
        return sb.toString()
    }

}
