package bamboo.component.service;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tangshuai on 2018/3/17.
 */
public class ServiceRegistryTest {

    private class ComponentOutputA {

    }

    private class ComponentOutputB {

    }

    ServiceRegistry serviceRegistry = new ServiceRegistry();

    @Test
    public void registerComponentOutput() {

        ComponentOutputA componentOutputA = new ComponentOutputA();
        ComponentOutputB componentOutputB = new ComponentOutputB();
        serviceRegistry.register(ComponentOutputA.class, componentOutputA);
        serviceRegistry.register(ComponentOutputB.class, componentOutputB);
        serviceRegistry.register(ComponentOutputB.class, componentOutputB);
        assertEquals(serviceRegistry.serviceMap.size(), 2);

        assertEquals(serviceRegistry.search(ComponentOutputA.class), componentOutputA);
        assertEquals(serviceRegistry.search(ComponentOutputB.class), componentOutputB);
    }

}