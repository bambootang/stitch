package bamboo.stitch.router.compiler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tangshuai on 2018/3/30.
 */

public class ActivityPageBinding {

    String activityPageName;

    String activityPageClass;

    Map<String, String> imports = new HashMap<>();

    List<ParameterBinding> parameters = new ArrayList<>();

    List<ConstructorBinding> constructors = new ArrayList<>();

}
