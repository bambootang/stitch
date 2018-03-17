package bamboo.component.datarouter;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tangshuai on 2018/3/17.
 */
public class ComponentRouterRegistryTest {

    private class ComponentOutputA implements ComponentOutput {

    }

    private class ComponentOutputB implements ComponentOutput {

    }

    ComponentRouterRegistry componentRouterRegistry = new ComponentRouterRegistry();

    @Test
    public void registerComponentOutput() {

        ComponentOutputA componentOutputA = new ComponentOutputA();
        ComponentOutputB componentOutputB = new ComponentOutputB();
        componentRouterRegistry.register(ComponentOutputA.class, componentOutputA);
        componentRouterRegistry.register(ComponentOutputB.class, componentOutputB);
        componentRouterRegistry.register(ComponentOutputB.class, componentOutputB);
        assertEquals(componentRouterRegistry.routerMap.size(),2);

        assertEquals(componentRouterRegistry.search(ComponentOutputA.class),componentOutputA);
        assertEquals(componentRouterRegistry.search(ComponentOutputB.class),componentOutputB);
    }

}