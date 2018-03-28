package bamboo.component.lifecycle;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by tangshuai on 2018/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class ComponentLifeRegistryTest {

    MockComponentALife aApplication = new MockComponentALife();
    MockComponentBLife bApplication = new MockComponentBLife();

    ComponentLifeRegistry componentLifeRegistry = new ComponentLifeRegistry();

    /**
     * 验证顺序，MockComponentAApplication的level优先于MockComponentAApplication的level
     */
    @Test
    public void aisbeforB() {
        componentLifeRegistry.register("aApplication", aApplication);
        componentLifeRegistry.register("aApplication", aApplication);
        componentLifeRegistry.register("aApplication", aApplication);
        componentLifeRegistry.register("bApplication", bApplication);
        componentLifeRegistry.register("bApplication", bApplication);
        componentLifeRegistry.register("bApplication", bApplication);
        Iterable<ComponentLife> applications = componentLifeRegistry.getAll();
        Iterator<ComponentLife> iterator = applications.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(iterator.next(), aApplication);
        assertEquals(iterator.next(), bApplication);
        assertTrue(!iterator.hasNext());
    }

    @Test
    public void lifeCycleInvoke() {
        aApplication = Mockito.mock(MockComponentALife.class);
        bApplication = Mockito.mock(MockComponentBLife.class);
        componentLifeRegistry.register("aApplication", aApplication);
        componentLifeRegistry.register("bApplication", bApplication);
    }
}