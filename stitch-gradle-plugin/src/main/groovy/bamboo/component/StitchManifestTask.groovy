package bamboo.component

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskAction

class StitchManifestTask {


    String variantName

    String manifestPath

    List<String> sourceAptDirs = new ArrayList<>()

    void addSourceAptDir(String sourceAptDir) {
        sourceAptDirs.add(sourceAptDir)
    }

    void processManifestComponent(Project project) {

        XmlParser xmlParser = new XmlParser()
        Node node = xmlParser.parse(manifestPath)


        for (String sourceAptDir : sourceAptDirs) {
            File file = project.file(sourceAptDir)
            println "file.absolutePath = ${file.absolutePath}"
            List<String> components = new ArrayList<>()
            File[] files = file.listFiles()
            for (File f : files) {
                findAllBindingFile(f, "", components)
            }

            for (Node n : node.children()) {
                if (n.name() == 'application') {
                    for (String component : components) {
                        println "component = $component"
                        Map<String, Object> attributes = new HashMap<>()
                        attributes.put("android:value", "ComponentApplication")
                        attributes.put("android:name", component)
                        n.appendNode("meta-data", attributes)
                    }
                    break
                }
            }
        }

        FileWriter fileWriter = new FileWriter(manifestPath)
        XmlNodePrinter xmlNodePrinter = new XmlNodePrinter(new PrintWriter(fileWriter))
        xmlNodePrinter.print(node)
        fileWriter.close()
    }

    private void findAllBindingFile(File file, String dir, List<String> components) {
        if (file.isFile() && file.name.endsWith("_ComponentBinding.java")) {
            components.add(dir + file.name.replace(".java", ""))
        } else {
            File[] files = file.listFiles()
            for (File f : files) {
                findAllBindingFile(f, dir + file.name + ".", components)
            }
        }
    }

}