package bamboo.component.stitch.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记为外部可访问的Activity
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface Exported {

    /**
     * @return Activity所对应的 ActivityPage 的类名
     */
    Class value();

}
