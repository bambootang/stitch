package bamboo.component

import com.android.build.gradle.api.BaseVariant
import org.gradle.api.Plugin
import org.gradle.api.Project

class StitchPlugin implements Plugin<Project> {


    @Override
    void apply(Project project) {


        project.afterEvaluate {
            if (project.plugins.hasPlugin("com.android.application")) {
                applyApplication(project)
            } else if (project.plugins.hasPlugin("com.android.library")) {
                applyLibrary(project)
            }
        }

        try {
            project.dependencies {
                implementation 'bamboo.components.stitch:stitch-core:1.0'
                annotationProcessor 'bamboo.components.stitch:stitch-compiler:1.0'
            }
        } catch (Exception e) {
            e.printStackTrace()
        }
    }


    static void applyLibrary(Project project) {

        for (BaseVariant variant : project.extensions.android.libraryVariants) {

            def stitcheTask = project.tasks.create("stitch${variant.name.capitalize()}Resources", StitcheTask.class)

            def variantOutput = variant.outputs.first()

            if (variantOutput.processManifest.properties['manifestOutputDirectory'] != null) {
                stitcheTask.manifestFileDir = project.file(variantOutput.processManifest.properties['manifestOutputDirectory'].toString())
            } else if (variantOutput.processResources.properties['manifestFile'] != null) {
                stitcheTask.manifestFileDir = project.file(variantOutput.processResources.properties['manifestFile'].toString())
            }

            stitcheTask.mustRunAfter variantOutput.processManifest

            def packageResource = project.tasks.findByName("package${variant.name.capitalize()}Resources")
            if (packageResource != null) {
                packageResource.dependsOn stitcheTask
            } else {
                variantOutput.processResources.dependsOn stitcheTask
            }
        }
    }

    static void applyApplication(Project project) {

        for (BaseVariant variant : project.extensions.android.applicationVariants) {

            def stitcheTask = project.tasks.create("stitch${variant.name.capitalize()}Resources", StitcheTask.class)

            def variantOutput = variant.outputs.first()

            if (variantOutput.processManifest.properties['manifestOutputDirectory'] != null) {
                stitcheTask.manifestFileDir = project.file(variantOutput.processManifest.properties['manifestOutputDirectory'].toString())
            } else if (variantOutput.processResources.properties['manifestFile'] != null) {
                stitcheTask.manifestFileDir = project.file(variantOutput.processResources.properties['manifestFile'].toString())
            }

            stitcheTask.mustRunAfter variantOutput.processManifest

            def packageResource = project.tasks.findByName("package${variant.name.capitalize()}Resources")
            if (packageResource != null) {
                packageResource.dependsOn stitcheTask
            } else {
                variantOutput.processResources.dependsOn stitcheTask
            }
        }
    }

}