package bamboo.component;

import android.content.res.Configuration;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import bamboo.component.lifecycle.ComponentLife;
import bamboo.component.lifecycle.ComponentLifeRegistry;
import bamboo.component.lifecycle.ComponentPriority;

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
    public ComponentLife aApplication;

    @Mock
    public ComponentLife bApplication;

    @Mock
    public Configuration configuration;

    ComponentLifeRegistry componentLifeRegistry = new ComponentLifeRegistry();

    @Before
    public void _start() {
    }


    @Test
    public void lifeCycleInvoke() {
        componentLifeRegistry.register("aApplication", aApplication);
        componentLifeRegistry.register("aApplication", aApplication);
        componentLifeRegistry.register("aApplication", aApplication);
        componentLifeRegistry.register("bApplication", bApplication);
        StitcherHelper.setComponentLifeRegistry(componentLifeRegistry);

        StitcherHelper.onCreate();
        verify(aApplication, times(1)).onCreate();
        verify(bApplication, times(1)).onCreate();

        StitcherHelper.onConfigurationChanged(configuration);
        verify(aApplication, times(1)).onConfigurationChanged(configuration);
        verify(bApplication, times(1)).onConfigurationChanged(configuration);

        StitcherHelper.onCreateDelay();
        verify(aApplication, times(1)).onCreateDelay();
        verify(bApplication, times(1)).onCreateDelay();

        StitcherHelper.onLowMemory();
        verify(aApplication, times(1)).onLowMemory();
        verify(bApplication, times(1)).onLowMemory();

        StitcherHelper.onTrimMemory(1);
        verify(aApplication, times(1)).onTrimMemory(1);
        verify(bApplication, times(1)).onTrimMemory(1);
    }

}