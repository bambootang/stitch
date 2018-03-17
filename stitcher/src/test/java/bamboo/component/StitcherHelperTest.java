package bamboo.component;

import android.content.res.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import bamboo.component.datarouter.ComponentRouterRegistry;
import bamboo.component.lifecycle.ComponentApplication;
import bamboo.component.lifecycle.ComponentLifeRegistry;
import bamboo.component.lifecycle.ComponentPriority;
import bamboo.component.pagerouter.ActivityRouterRegistry;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by tangshuai on 2018/3/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class StitcherHelperTest {
    @Mock
    public ComponentApplication aApplication;

    @Mock
    public ComponentApplication bApplication;

    @Mock
    public Configuration configuration;

    ComponentLifeRegistry componentLifeRegistry = new ComponentLifeRegistry();

    @Before
    public void _start() {
        when(aApplication.level()).thenReturn(ComponentPriority.LEVEL_HIGH);
        when(bApplication.level()).thenReturn(ComponentPriority.LEVEL_LOW);
    }


    @Test
    public void lifeCycleInvoke() {
        componentLifeRegistry.register(aApplication);
        componentLifeRegistry.register(aApplication);
        componentLifeRegistry.register(aApplication);
        componentLifeRegistry.register(bApplication);
        StitcherHelper.setComponentLifeRegistry(componentLifeRegistry);

        StitcherHelper.onCreate();
        verify(aApplication, times(1)).onCreate();
        verify(bApplication, times(1)).onCreate();

        StitcherHelper.onConfigurationChanged(configuration);
        verify(aApplication, times(1)).onConfigurationChanged(configuration);
        verify(bApplication, times(1)).onConfigurationChanged(configuration);

        StitcherHelper.onCreateDelay();
        verify(aApplication, times(1)).onCreateDelay(any(ComponentRouterRegistry.class), any(ActivityRouterRegistry.class));
        verify(bApplication, times(1)).onCreateDelay(any(ComponentRouterRegistry.class), any(ActivityRouterRegistry.class));

        StitcherHelper.onLowMemory();
        verify(aApplication, times(1)).onLowMemory();
        verify(bApplication, times(1)).onLowMemory();

        StitcherHelper.onTrimMemory(1);
        verify(aApplication, times(1)).onTrimMemory(1);
        verify(bApplication, times(1)).onTrimMemory(1);
    }

}