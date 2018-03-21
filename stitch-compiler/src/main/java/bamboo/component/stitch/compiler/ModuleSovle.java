package bamboo.component.stitch.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.JavaFileObject;

/**
 * Created by tangshuai on 2018/3/21.
 */

public class ModuleSovle {

    JavaFileObject javaFileObject;
    String fileName;

    private String variantName;
    private String moduleDir;
    private String moduleName;

    ///Users/tangshuai/DemoProjects/Stitcher/stitch-compiler-test/build/generated/source/apt/debug

    public ModuleSovle(JavaFileObject javaFileObject, String fileName) {
        this.javaFileObject = javaFileObject;
        this.fileName = fileName;
        parseModule();
    }

    private void parseModule() {
        File file = new File(javaFileObject.toUri().getPath());
        System.out.println(fileName);
        String outdir = file.getAbsolutePath().replace(fileName.replace(".", File.separator) + ".java", "");
        outdir = outdir.substring(0, outdir.length() - 1);
        System.out.println(outdir);
        variantName = outdir.substring(outdir.lastIndexOf(File.separator) + 1, outdir.length());
        System.out.println(variantName);
        File moduleDirFile = new File(outdir)//variantName
                .getParentFile()//apt
                .getParentFile()//source
                .getParentFile()//generated
                .getParentFile()//build
                .getParentFile();//module_base_dir
        moduleDir = moduleDirFile.getAbsolutePath();
        moduleName = moduleDirFile.getName();
    }

    public String findPackageName() {
        String manifestsDir = moduleDir
                + File.separator + "build"
                + File.separator + "intermediates"
                + File.separator + "manifests"
                + File.separator + "full"
                + File.separator + variantName;
        try {
            Pattern pattern = Pattern.compile("package=\"\\S+\"");
            FileReader fileReader = new FileReader(manifestsDir + File.separator + "AndroidManifest.xml");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String s;
            while ((s = bufferedReader.readLine()) != null) {
                System.out.println(s);
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    String matcherS = matcher.group();
                    return matcherS.substring(9, matcherS.length() - 1);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getModuleName() {
        return Name.toCamlStyle(moduleName);
    }


}
