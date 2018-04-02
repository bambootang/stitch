package bamboo.stitch.router.compiler;

import java.lang.annotation.ElementType;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by tangshuai on 2018/4/1.
 */

public class ParameterChecks {


    static void checkModifier(Element element, Modifier modifier) {
        if (!element.getModifiers().contains(modifier)) {
            StringBuilder sb = new StringBuilder("@Wrapper error : ");
            sb.append(element.getSimpleName());
            sb.append(" must be ");
            sb.append(modifier.name());
            sb.append(";");
            throw new IllegalStateException(sb.toString());
        }
    }

    static void checkNoneModifier(Element element, Modifier modifier) {
        if (element.getModifiers().contains(modifier)) {
            StringBuilder sb = new StringBuilder("@Parameter error : ");
            sb.append(element.getSimpleName());
            sb.append(" can not be ");
            sb.append(modifier.name());
            sb.append(";");
            throw new IllegalStateException(sb.toString());
        }
    }

}
