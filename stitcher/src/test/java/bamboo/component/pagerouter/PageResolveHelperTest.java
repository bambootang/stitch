package bamboo.component.pagerouter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.HashMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by tangshuai on 2018/3/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class PageResolveHelperTest {

    @Test
    public void resolvePageConsumer() throws Exception {
        Object pageConsume = new TestPageConsume();

        HashMap<Class, Method> methodHashMap = new HashMap<>(8);
        HashMap<Class, Object> holderHashMap = new HashMap<>(8);
        PageResolveHelper.resolvePageConsumer(pageConsume, methodHashMap, holderHashMap);

        assertEquals(methodHashMap.size(), 2);
        assertEquals(holderHashMap.size(), 2);
        assertThat(holderHashMap.get(TestPageConsume.TestPage.class), is(pageConsume));
        assertThat(holderHashMap.get(TestPageConsume.TestPage2.class), is(pageConsume));
        assertNotNull(methodHashMap.get(TestPageConsume.TestPage.class));
        assertNotNull(methodHashMap.get(TestPageConsume.TestPage2.class));
        assertNull(methodHashMap.get(ActivityPage.class));
        assertNull(holderHashMap.get(ActivityPage.class));
    }

}