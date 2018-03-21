package bamboo.component

import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.tasks.ProcessAndroidResources
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.internal.DefaultDomainObjectSet

import java.lang.reflect.Method

class StitchPlugin implements Plugin<Project> {

//    StitchManifestTask manifestTask = new StitchManifestTask()
    HashMap<String, StitchManifestTask> manifestTaskHashMap = new HashMap<>();

    Project project

    @Override
    void apply(Project project) {

        this.project = project

        project.afterEvaluate {

            applyMainProject()
            if (project.plugins.hasPlugin("com.android.application"))
                applySubProjects()

        }
        project.dependencies {
            implementation 'bamboo.components.stitch:stitch-core:1.0'
            annotationProcessor 'bamboo.components.stitch:stitch-compiler:1.0'
        }
    }

    void applySubProjects() {


        List<String> subPs = new ArrayList<>();
        project.extensions.android.applicationVariants.all { variant ->
            Configuration configuration = variant.getCompileConfiguration()
            DependencySet dependencySet = configuration.getAllDependencies()
            println dependencySet
            dependencySet.all {
                if (it.class.simpleName.contains("DefaultProjectDependency")) {
                    subPs.add(it.name)
                }
//                println "${it.class.simpleName.contains("DefaultProjectDependency")}"
//                println "${it.getGroup()}-${it.name}-${it.version}-${it.class}"
            }
        }

        subPs.each {
            println it
        }

        println "-----------------"

        project.rootProject.allprojects { subProject ->

            subProject.afterEvaluate {

                println subProject.name
                println "subProject.plugins.hasPlugin(\"com.android.library\") = ${subProject.plugins.hasPlugin("com.android.library")}"
                println "subPs.contains(subProject.name) = ${subPs.contains(subProject.name)}"
                if (subPs.contains(subProject.name) && subProject.plugins.hasPlugin("com.android.library")) {

                    DefaultDomainObjectSet<BaseVariant> variants = subProject.extensions.android.libraryVariants

                    variants.all { variant ->

                        def variantName = variant.name.capitalize()
                        println "-${subProject.name}-${variantName}"
                        if (manifestTaskHashMap.containsKey(variantName)) {

                            println "${manifestTaskHashMap.get(variantName)}"

                            def javaCompilerTask = variant.javaCompiler
                            def sourceAptDir = ""
                            for (File file : javaCompilerTask.outputs.files) {
                                println "${subProject.name}-output-${file.absolutePath}"
                                if (file.absolutePath.contains("source/apt")) {
                                    sourceAptDir = file.absolutePath
                                }
                            }
                            manifestTaskHashMap.get(variantName).addSourceAptDir(sourceAptDir)
                        } else {

                        }
                    }

                }
            }
        }

//        project.childProjects.each {
//            k, subProject ->
//
//                if (subProject.plugins.hasPlugin("com.android.library")) {
//
//                    DefaultDomainObjectSet<BaseVariant> variants = subProject.extensions.android.libraryVariants
//
//                    variants.all { variant ->
//
//                        def variantName = variant.name.capitalize()
//                        println "-${subProject.name}-${variantName}"
//                        if (manifestTaskHashMap.containsKey(variantName)) {
//
//                            println "${manifestTaskHashMap.get(variantName)}"
//
//                            def javaCompilerTask = variant.javaCompiler
//                            def sourceAptDir = ""
//                            for (File file : javaCompilerTask.outputs.files) {
//                                println "${subProject.name}-output-${file.absolutePath}"
//                                if (file.absolutePath.contains("source/apt")) {
//                                    sourceAptDir = file.absolutePath
//                                }
//                            }
//                            manifestTaskHashMap.get(variantName).addSourceAptDir(sourceAptDir)
//                        } else {
//
//                        }
//                    }
//                }
//        }
    }

    void applyMainProject() {

        DefaultDomainObjectSet<BaseVariant> variants
        Task bundleTask

        if (project.plugins.hasPlugin("com.android.library")) {
            variants = project.extensions.android.libraryVariants
        } else if (project.plugins.hasPlugin("com.android.application")) {
            variants = project.extensions.android.applicationVariants
        } else {
            throw new IllegalAccessException("stitch.plugin work only with plugin com.android.application or com.android.library")
        }
        variants.all { variant ->
            def variantOutput = variant.outputs.first()
            def variantName = variant.name.capitalize()

            println "${project.name}-${variantName}"
            StitchManifestTask manifestTask = new StitchManifestTask()
            manifestTaskHashMap.put(variantName, manifestTask)

//                if (project.plugins.hasPlugin("com.android.library")) {
            bundleTask = project.tasks.findByName("compile${variantName}JavaWithJavac")
//                } else if (project.plugins.hasPlugin("com.android.application")) {
//                    bundleTask = project.tasks.findByName("package${variantName}")
//                }
//                def preAssembleTask = project.tasks.findByName("process${variantName}Manifest")
            def javaCompilerTask = variant.javaCompiler
            def sourceAptDir = ""
            for (File file : javaCompilerTask.outputs.files) {
                if (file.absolutePath.contains("source/apt")) {
                    sourceAptDir = file.absolutePath
                }
            }
//                StitchManifestTask manifestTask = project.tasks.create("stitchProcess${variantName}ManifestTask", StitchManifestTask.class)

            manifestTask.variantName = variantName
            manifestTask.addSourceAptDir(sourceAptDir)

            manifestTask.getProperties().put("manifestPath", variantOutput.processResources.properties['manifestFile'])
            if (variantOutput.processManifest.properties['manifestOutputDirectory'] != null) {
                manifestTask.manifestPath = variantOutput.processManifest.properties['manifestOutputDirectory'].toString() + "/AndroidManifest.xml"
            } else if (variantOutput.processResources.properties['manifestFile'] != null) {
                manifestTask.manifestPath = variantOutput.processResources.properties['manifestFile']
            }
//                manifestTask.mustRunAfter variantOutput.processManifest
//                bundleTask.dependsOn manifestTask
//                manifestTask.dependsOn bundleTask
            variantOutput.processResources.doFirst {
                manifestTask.processManifestComponent(project)
            }
            bundleTask.doLast {
                manifestTask.processManifestComponent(project)
            }

            Task packageTask = project.tasks.findByName("package${variantName}")
            Task processResources = project.tasks.findByName("process${variantName}Resources")
            if (packageTask != null && processResources != null) {
                packageTask.doFirst {
                    try {
                        Method method = ProcessAndroidResources.class.getDeclaredMethod("doFullTaskAction")
                        method.setAccessible(true)
                        method.invoke(processResources)
                    } catch (Exception e) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

}