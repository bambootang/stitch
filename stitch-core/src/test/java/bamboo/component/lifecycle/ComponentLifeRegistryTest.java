package bamboo.component.lifecycle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by tangshuai on 2018/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ComponentLifeRegistryTest {

    MockComponentAApplication aApplication = new MockComponentAApplication();
    MockComponentBApplication bApplication = new MockComponentBApplication();

    ComponentLifeRegistry componentLifeRegistry = new ComponentLifeRegistry();

    /**
     * 验证顺序，MockComponentAApplication的level优先于MockComponentAApplication的level
     */
    @Test
    public void aisbeforB() {
        componentLifeRegistry.register(aApplication);
        componentLifeRegistry.register(aApplication);
        componentLifeRegistry.register(aApplication);
        componentLifeRegistry.register(bApplication);
        componentLifeRegistry.register(bApplication);
        componentLifeRegistry.register(bApplication);
        Collection<ComponentApplication> applications = componentLifeRegistry.getAll();
        assertEquals(applications.size(), 2);
        assertEquals(applications.toArray()[0], aApplication);
        assertEquals(applications.toArray()[1], bApplication);
    }

    @Test
    public void lifeCycleInvoke(){
        aApplication = Mockito.mock(MockComponentAApplication.class);
        bApplication = Mockito.mock(MockComponentBApplication.class);
        componentLifeRegistry.register(aApplication);
        componentLifeRegistry.register(bApplication);
    }
}