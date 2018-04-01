package bamboo.component.service;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tangshuai on 2018/3/17.
 */
public class ServiceRegistryTest {


    public static class ComponentOutputA {

    }

    public static class ComponentOutputB {

    }

    private ServiceRegistry serviceRegistry = new ServiceRegistry();

    @Test
    public void registerComponentOutput() {

        serviceRegistry.register(ComponentOutputA.class, ComponentOutputA.class.getName());
        serviceRegistry.register(ComponentOutputB.class, ComponentOutputB.class.getName());
        serviceRegistry.register(ComponentOutputB.class, ComponentOutputB.class.getName());
        assertEquals(serviceRegistry.serviceMap.size(), 2);

        ComponentOutputA componentOutputA1 = serviceRegistry.search(ComponentOutputA.class);
        ComponentOutputA componentOutputA2 = serviceRegistry.search(ComponentOutputA.class);

        ComponentOutputB componentOutputB1 = serviceRegistry.search(ComponentOutputB.class);
        ComponentOutputB componentOutputB2 = serviceRegistry.search(ComponentOutputB.class);

        assertEquals(componentOutputA1, componentOutputA2);
        assertEquals(componentOutputB1, componentOutputB2);

    }

}