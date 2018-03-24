package bamboo.component.stitch.compiler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.tools.FileObject;
import javax.tools.JavaFileObject;

/**
 * Created by tangshuai on 2018/3/21.
 */

public class ModuleSovle {

    FileObject javaFileObject;
    String fileName;

    private String variantName;
    private String moduleDir;
    private String moduleName;
    private String flavorName;


    public ModuleSovle(FileObject javaFileObject, String fileName) {
        this.javaFileObject = javaFileObject;
        this.fileName = fileName;
        parseModule();
    }

    private void parseModule() {
        File file = new File(javaFileObject.toUri().getPath());
        File variantOutFile = new File(file.getAbsolutePath().replace(fileName, ""));
        variantName = variantOutFile.getName();
        File outdir = variantOutFile;
        if (!variantOutFile.getParentFile().getName().equals("apt")) {
            flavorName = variantOutFile.getParentFile().getName();
            outdir = variantOutFile.getParentFile();
        }
        File moduleDirFile = outdir//variantName
                .getParentFile()//apt
                .getParentFile()//source
                .getParentFile()//generated
                .getParentFile()//build
                .getParentFile();//module_base_dir
        moduleDir = moduleDirFile.getAbsolutePath();
        moduleName = moduleDirFile.getName();
//        System.out.println(fileName);
//        System.out.println(moduleDir);
//        System.out.println(moduleName);
//        System.out.println(flavorName);
        try {
            javaFileObject.openWriter().close();
            if (new File(javaFileObject.toUri().getPath()).delete()) {
//                System.out.println(" javaFileObject.delete() ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String findPackageName() {
        String manifestsDir = moduleDir
                + File.separator + "build"
                + File.separator + "intermediates"
                + File.separator + "manifests"
                + File.separator + "full"
                + (flavorName == null ? "" : (File.separator + flavorName))
                + File.separator + variantName;
        try {
            Pattern pattern = Pattern.compile("package=\"\\S+\"");
            FileReader fileReader = new FileReader(manifestsDir + File.separator + "AndroidManifest.xml");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String s;
            while ((s = bufferedReader.readLine()) != null) {
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
