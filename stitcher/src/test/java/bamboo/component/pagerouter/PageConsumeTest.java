package bamboo.component.pagerouter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import bamboo.component.StitcherHelper;

import static junit.framework.Assert.assertEquals;

/**
 * Created by tangshuai on 2018/3/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class PageConsumeTest {


    @Test
    public void startTestPage() {
        TestPageConsume consume = new TestPageConsume();
        StitcherHelper.regiestPageConumer(consume);
        StitcherHelper.start(new TestPageConsume.TestPage2(null));
        assertEquals(consume.msg, "TestPage2");

    }

    @Test
    public void startTestPage2() {
        TestPageConsume consume = new TestPageConsume();
        StitcherHelper.regiestPageConumer(consume);

        StitcherHelper.start(new TestPageConsume.TestPage(null));
        assertEquals(consume.msg, "TestPage");
    }

    @Test
    public void startTestPage3NotConsume() {
        TestPageConsume consume = new TestPageConsume();
        StitcherHelper.regiestPageConumer(consume);

        try {
            StitcherHelper.start(new TestPageConsume.TestPage3(null));
        } catch (Exception e) {

        }
        assertEquals(consume.msg, null);
    }

    @Test
    public void startBasePageNotConsume() {
        TestPageConsume consume = new TestPageConsume();
        StitcherHelper.regiestPageConumer(consume);

        assertEquals(consume.msg, null);
    }
}